package pl.edu.ecommerceshop.catalog.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponse(
        Long id,
        String sku,
        String name,
        String description,
        String imageUrl,
        BigDecimal price,
        int stockQuantity,
        int reservedQuantity,
        int availableQuantity,
        boolean active,
        CategoryResponse category,
        Instant createdAt,
        Instant updatedAt
) {
}
