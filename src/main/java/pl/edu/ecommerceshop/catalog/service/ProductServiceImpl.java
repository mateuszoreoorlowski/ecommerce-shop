package pl.edu.ecommerceshop.catalog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.ecommerceshop.catalog.assets.ProductAssetUrlResolver;
import pl.edu.ecommerceshop.catalog.assets.ProductImageStorageService;
import pl.edu.ecommerceshop.catalog.dto.*;
import pl.edu.ecommerceshop.catalog.model.Category;
import pl.edu.ecommerceshop.catalog.model.Product;
import pl.edu.ecommerceshop.catalog.repository.CategoryRepository;
import pl.edu.ecommerceshop.catalog.repository.ProductRepository;
import pl.edu.ecommerceshop.common.dto.PageResponse;
import pl.edu.ecommerceshop.common.exception.BusinessException;
import pl.edu.ecommerceshop.common.exception.ResourceNotFoundException;
import pl.edu.ecommerceshop.inventory.model.StockMovement;
import pl.edu.ecommerceshop.inventory.model.StockMovementType;
import pl.edu.ecommerceshop.inventory.repository.StockMovementRepository;

import static pl.edu.ecommerceshop.configuration.config.RedisCacheConfig.PRODUCTS_CACHE;
import static pl.edu.ecommerceshop.configuration.config.RedisCacheConfig.PRODUCT_CACHE;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final StockMovementRepository stockMovementRepository;
    private final ProductAssetUrlResolver productAssetUrlResolver;
    private final ProductImageStorageService productImageStorageService;

    @Transactional(readOnly = true)
    @Override
    @Cacheable(cacheNames = PRODUCTS_CACHE, keyGenerator = "catalogCacheKeyGenerator")
    public PageResponse<ProductResponse> getProductsList(
            String query,
            String categorySlug,
            Boolean active,
            Pageable pageable
    ) {
        Page<ProductResponse> page = productRepository.searchProducts(
                        toSearchPattern(query),
                        normalizeOptional(categorySlug),
                        active,
                        pageable
                )
                .map(CatalogMapper::mapToProductResponse);

        return PageResponse.from(page);
    }

    @Transactional(readOnly = true)
    @Override
    @Cacheable(cacheNames = PRODUCT_CACHE, key = "#id")
    public ProductResponse getProductById(Long id) {
        return CatalogMapper.mapToProductResponse(findProduct(id));
    }

    @Transactional
    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = PRODUCTS_CACHE, allEntries = true),
            @CacheEvict(cacheNames = PRODUCT_CACHE, allEntries = true)
    })
    public ProductResponse createProductWithImage(ProductCreateMultipartRequest request) {
        if (productRepository.existsBySku(request.sku())) {
            throw new BusinessException("Product SKU already exists.");
        }

        categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category with id %d not found.".formatted(request.categoryId())
                ));

        String uploadedImagePath = productImageStorageService.uploadProductImage(request.image());

        ProductCreateRequest createRequest = new ProductCreateRequest(
                request.sku(),
                request.name(),
                request.description(),
                uploadedImagePath,
                request.price(),
                request.stockQuantity(),
                request.categoryId()
        );

        return createProduct(createRequest);
    }

    @Transactional
    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = PRODUCT_CACHE, key = "#id"),
            @CacheEvict(cacheNames = PRODUCTS_CACHE, allEntries = true)
    })
    public ProductResponse updateProductWithImage(Long id, ProductUpdateMultipartRequest request) {
        findProduct(id);

        if (request.categoryId() != null) {
            categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Category with id %d not found.".formatted(request.categoryId())
                    ));
        }

        if (Boolean.TRUE.equals(request.removeImage())
                && request.image() != null
                && !request.image().isEmpty()) {
            throw new BusinessException("Cannot upload and remove product image at the same time.");
        }

        String imageUrl = null;

        if (Boolean.TRUE.equals(request.removeImage())) {
            imageUrl = "";
        } else if (request.image() != null && !request.image().isEmpty()) {
            imageUrl = productImageStorageService.uploadProductImage(request.image());
        }

        ProductUpdateRequest updateRequest = new ProductUpdateRequest(
                request.name(),
                request.description(),
                imageUrl,
                request.price(),
                request.active(),
                request.categoryId()
        );

        return updateProduct(id, updateRequest);
    }

    @Transactional
    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = PRODUCTS_CACHE, allEntries = true),
            @CacheEvict(cacheNames = PRODUCT_CACHE, allEntries = true)
    })
    public ProductResponse createProduct(ProductCreateRequest request) {
        if (productRepository.existsBySku(request.sku())) {
            throw new BusinessException("Product SKU already exists.");
        }

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category with id %d not found.".formatted(request.categoryId())
                ));

        Product product = new Product(
                request.sku(),
                request.name(),
                request.description(),
                productAssetUrlResolver.normalizeImageUrl(request.imageUrl()),
                request.price(),
                request.stockQuantity(),
                category
        );

        Product saved = productRepository.save(product);

        if (request.stockQuantity() > 0) {
            stockMovementRepository.save(new StockMovement(
                    saved.getId(),
                    saved.getSku(),
                    StockMovementType.INITIAL_STOCK,
                    request.stockQuantity(),
                    "Initial stock during product creation",
                    null
            ));
        }

        return CatalogMapper.mapToProductResponse(saved);
    }

    @Transactional
    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = PRODUCT_CACHE, key = "#id"),
            @CacheEvict(cacheNames = PRODUCTS_CACHE, allEntries = true)
    })
    public ProductResponse updateProduct(Long id, ProductUpdateRequest request) {
        Product product = findProduct(id);

        Category category = null;
        if (request.categoryId() != null) {
            category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Category with id %d not found.".formatted(request.categoryId())
                    ));
        }

        String normalizedImageUrl = null;

        if (request.imageUrl() != null) {
            normalizedImageUrl = request.imageUrl().isBlank()
                    ? ""
                    : productAssetUrlResolver.normalizeImageUrl(request.imageUrl());
        }

        product.updateDetails(
                request.name(),
                request.description(),
                normalizedImageUrl,
                request.price(),
                request.active(),
                category
        );

        return CatalogMapper.mapToProductResponse(product);
    }

    @Transactional
    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = PRODUCT_CACHE, key = "#id"),
            @CacheEvict(cacheNames = PRODUCTS_CACHE, allEntries = true)
    })
    public ProductResponse receiveStock(Long id, ReceiveStockRequest request) {
        Product product = productRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id %d not found.".formatted(id)
                ));

        product.receiveStock(request.quantity());

        stockMovementRepository.save(new StockMovement(
                product.getId(),
                product.getSku(),
                StockMovementType.STOCK_RECEIVED,
                request.quantity(),
                request.reason(),
                null
        ));

        return CatalogMapper.mapToProductResponse(product);
    }

    @Transactional(readOnly = true)
    @Override
    public Product findProduct(Long id) {
        return productRepository.findByIdWithCategory(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id %d not found.".formatted(id)
                ));
    }

    private String toSearchPattern(String query) {
        if (query == null || query.isBlank()) {
            return "%%";
        }

        return "%" + query.trim().toLowerCase() + "%";
    }

    private String normalizeOptional(String value) {
        return value == null || value.isBlank()
                ? null
                : value.trim();
    }
}