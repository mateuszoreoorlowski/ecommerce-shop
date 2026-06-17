package pl.edu.ecommerceshop.catalog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.ecommerceshop.catalog.dto.ProductCreateRequest;
import pl.edu.ecommerceshop.catalog.dto.ProductResponse;
import pl.edu.ecommerceshop.catalog.dto.ProductUpdateRequest;
import pl.edu.ecommerceshop.catalog.dto.ReceiveStockRequest;
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

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final StockMovementRepository stockMovementRepository;

    @Transactional(readOnly = true)
    @Override
    public PageResponse<ProductResponse> getProductsList(String query, String categorySlug, Boolean active, Pageable pageable) {
        Specification<Product> specification = (_, _, cb) -> cb.conjunction();
        if (query != null && !query.isBlank()) {
            String pattern = "%" + query.toLowerCase() + "%";
            specification = specification.and((root, _, cb) -> cb.or(
                    cb.like(cb.lower(root.get("name")), pattern),
                    cb.like(cb.lower(root.get("description")), pattern),
                    cb.like(cb.lower(root.get("sku")), pattern)
            ));
        }
        if (categorySlug != null && !categorySlug.isBlank()) {
            specification = specification.and((root, _, cb) -> cb.equal(root.get("category").get("slug"), categorySlug));
        }
        if (active != null) {
            specification = specification.and((root, _, cb) -> cb.equal(root.get("active"), active));
        }
        Page<ProductResponse> page = productRepository.findAll(specification, pageable).map(CatalogMapper::mapToProductResponse);
        return PageResponse.from(page);
    }

    @Transactional(readOnly = true)
    @Override
    public ProductResponse getProductById(Long id) {
        return CatalogMapper.mapToProductResponse(findProduct(id));
    }

    @Transactional
    @Override
    public ProductResponse createProduct(ProductCreateRequest request) {
        if (productRepository.existsBySku(request.sku())) {
            throw new BusinessException("Product SKU already exists.");
        }
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category with id %d not found.".formatted(request.categoryId())));
        Product product = new Product(request.sku(), request.name(), request.description(), request.price(), request.stockQuantity(), category);
        Product saved = productRepository.save(product);
        if (request.stockQuantity() > 0) {
            stockMovementRepository.save(new StockMovement(saved.getId(), saved.getSku(), StockMovementType.INITIAL_STOCK,
                    request.stockQuantity(), "Initial stock during product creation", null));
        }
        return CatalogMapper.mapToProductResponse(saved);
    }

    @Transactional
    @Override
    public ProductResponse updateProduct(Long id, ProductUpdateRequest request) {
        Product product = findProduct(id);
        Category category = null;
        if (request.categoryId() != null) {
            category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category with id %d not found.".formatted(request.categoryId())));
        }
        product.updateDetails(request.name(), request.description(), request.price(), request.active(), category);
        return CatalogMapper.mapToProductResponse(product);
    }

    @Transactional
    @Override
    public ProductResponse receiveStock(Long id, ReceiveStockRequest request) {
        Product product = productRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id %d not found.".formatted(id)));
        product.receiveStock(request.quantity());
        stockMovementRepository.save(new StockMovement(product.getId(), product.getSku(), StockMovementType.STOCK_RECEIVED,
                request.quantity(), request.reason(), null));
        return CatalogMapper.mapToProductResponse(product);
    }

    @Transactional(readOnly = true)
    @Override
    public Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id %d not found.".formatted(id)));
    }
}
