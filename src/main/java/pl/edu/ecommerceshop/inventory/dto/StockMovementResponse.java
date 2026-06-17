package pl.edu.ecommerceshop.inventory.dto;

import pl.edu.ecommerceshop.inventory.model.StockMovementType;

import java.time.Instant;

public record StockMovementResponse(
        Long id,
        Long productId,
        String productSku,
        StockMovementType type,
        int quantity,
        String reason,
        String orderNumber,
        Instant createdAt
) {
}
