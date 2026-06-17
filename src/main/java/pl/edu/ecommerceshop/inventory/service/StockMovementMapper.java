package pl.edu.ecommerceshop.inventory.service;

import pl.edu.ecommerceshop.inventory.dto.StockMovementResponse;
import pl.edu.ecommerceshop.inventory.model.StockMovement;

public final class StockMovementMapper {

    private StockMovementMapper() {
    }

    public static StockMovementResponse mapToStockMovementResponse(StockMovement movement) {
        return new StockMovementResponse(
                movement.getId(),
                movement.getProductId(),
                movement.getProductSku(),
                movement.getType(),
                movement.getQuantity(),
                movement.getReason(),
                movement.getOrderNumber(),
                movement.getCreatedAt()
        );
    }
}
