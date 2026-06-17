package pl.edu.ecommerceshop.catalog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.ecommerceshop.catalog.dto.CategoryPatchRequest;
import pl.edu.ecommerceshop.catalog.dto.CategoryRequest;
import pl.edu.ecommerceshop.catalog.dto.CategoryResponse;
import pl.edu.ecommerceshop.catalog.model.Category;
import pl.edu.ecommerceshop.catalog.repository.CategoryRepository;
import pl.edu.ecommerceshop.common.exception.BusinessException;
import pl.edu.ecommerceshop.common.exception.ResourceNotFoundException;

import java.util.List;

import static pl.edu.ecommerceshop.config.RedisCacheConfig.CATEGORIES_CACHE;
import static pl.edu.ecommerceshop.config.RedisCacheConfig.PRODUCTS_CACHE;
import static pl.edu.ecommerceshop.config.RedisCacheConfig.PRODUCT_CACHE;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    @Override
    @Cacheable(cacheNames = CATEGORIES_CACHE, key = "'all'")
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CatalogMapper::mapToCategoryResponse)
                .toList();
    }

    @Transactional
    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CATEGORIES_CACHE, allEntries = true),
            @CacheEvict(cacheNames = PRODUCTS_CACHE, allEntries = true),
            @CacheEvict(cacheNames = PRODUCT_CACHE, allEntries = true)
    })
    public CategoryResponse createCategory(CategoryRequest request) {
        validateUniqueness(request.name(), request.slug(), null);
        Category category = new Category(request.name(), request.slug());
        return CatalogMapper.mapToCategoryResponse(categoryRepository.save(category));
    }

    @Transactional
    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CATEGORIES_CACHE, allEntries = true),
            @CacheEvict(cacheNames = PRODUCTS_CACHE, allEntries = true),
            @CacheEvict(cacheNames = PRODUCT_CACHE, allEntries = true)
    })
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = getCategory(id);
        validateUniqueness(request.name(), request.slug(), category);
        category.update(request.name(), request.slug());
        return CatalogMapper.mapToCategoryResponse(category);
    }

    @Transactional(readOnly = true)
    @Override
    public Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id %d not found.".formatted(id)));
    }

    @Transactional
    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CATEGORIES_CACHE, allEntries = true),
            @CacheEvict(cacheNames = PRODUCTS_CACHE, allEntries = true),
            @CacheEvict(cacheNames = PRODUCT_CACHE, allEntries = true)
    })
    public CategoryResponse partialUpdateCategory(Long id, CategoryPatchRequest request) {
        Category category = getCategory(id);

        String newName = request.name() != null
                ? normalizeRequiredText(request.name(), "Category name")
                : category.getName();

        String newSlug = request.slug() != null
                ? normalizeRequiredText(request.slug(), "Category slug")
                : category.getSlug();

        validateUniqueness(newName, newSlug, category);

        category.update(newName, newSlug);

        return CatalogMapper.mapToCategoryResponse(category);
    }

    private String normalizeRequiredText(String value, String fieldName) {
        if (value == null || value.trim().isBlank()) {
            throw new BusinessException(fieldName + " cannot be blank.");
        }

        return value.trim();
    }

    private void validateUniqueness(String name, String slug, Category current) {
        boolean sameName = current != null
                && current.getName() != null
                && current.getName().equalsIgnoreCase(name);

        boolean sameSlug = current != null
                && current.getSlug() != null
                && current.getSlug().equals(slug);

        if (!sameName && categoryRepository.existsByNameIgnoreCase(name)) {
            throw new BusinessException("Category name already exists.");
        }

        if (!sameSlug && categoryRepository.existsBySlug(slug)) {
            throw new BusinessException("Category slug already exists.");
        }
    }
}