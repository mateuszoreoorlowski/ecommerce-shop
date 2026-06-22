package pl.edu.ecommerceshop.catalog.service;

import pl.edu.ecommerceshop.catalog.dto.CategoryResponse;
import pl.edu.ecommerceshop.catalog.dto.ProductResponse;
import pl.edu.ecommerceshop.catalog.model.Category;
import pl.edu.ecommerceshop.catalog.model.Product;

public final class CatalogMapper {

    private CatalogMapper() {
    }

    public static CategoryResponse mapToCategoryResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getSlug());
    }

    public static ProductResponse mapToProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getImageUrl(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getReservedQuantity(),
                product.availableQuantity(),
                product.isActive(),
                mapToCategoryResponse(product.getCategory()),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
