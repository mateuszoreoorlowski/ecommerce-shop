package pl.edu.ecommerceshop.inventory.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.edu.ecommerceshop.common.exception.BusinessException;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "stock_movements")
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false, length = 80)
    private String productSku;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 60)
    private StockMovementType type;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, length = 500)
    private String reason;

    @Column(length = 40)
    private String orderNumber;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected StockMovement() {
    }

    public StockMovement(
            Long productId,
            String productSku,
            StockMovementType type,
            int quantity,
            String reason,
            String orderNumber
    ) {
        if (productId == null) {
            throw new BusinessException("Product id cannot be null.");
        }

        if (productSku == null || productSku.isBlank()) {
            throw new BusinessException("Product SKU cannot be blank.");
        }

        if (type == null) {
            throw new BusinessException("Stock movement type cannot be null.");
        }

        if (quantity <= 0) {
            throw new BusinessException("Stock movement quantity must be greater than zero.");
        }

        if (reason == null || reason.isBlank()) {
            throw new BusinessException("Stock movement reason cannot be blank.");
        }

        if (requiresOrderNumber(type) && (orderNumber == null || orderNumber.isBlank())) {
            throw new BusinessException("Order number is required for this stock movement type.");
        }

        this.productId = productId;
        this.productSku = productSku.trim();
        this.type = type;
        this.quantity = quantity;
        this.reason = reason.trim();
        this.orderNumber = orderNumber != null && !orderNumber.isBlank()
                ? orderNumber.trim()
                : null;
    }

    @PrePersist
    void prePersist() {
        this.createdAt = Instant.now();
    }

    private boolean requiresOrderNumber(StockMovementType type) {
        return switch (type) {
            case RESERVATION_CREATED,
                 RESERVATION_RELEASED,
                 SALE_COMMITTED,
                 RETURN_RECEIVED -> true;

            case INITIAL_STOCK,
                 STOCK_RECEIVED,
                 STOCK_ADJUSTED,
                 DAMAGED -> false;
        };
    }
}