package pl.edu.ecommerceshop.order.model;

import jakarta.persistence.*;
import lombok.Getter;
import pl.edu.ecommerceshop.common.exception.BusinessException;

import java.math.BigDecimal;

@Entity
@Getter
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false, length = 80)
    private String productSku;

    @Column(nullable = false, length = 180)
    private String productName;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal lineTotal;

    protected OrderItem() {
    }

    public OrderItem(Order order, Long productId, String productSku, String productName, int quantity, BigDecimal unitPrice) {
        if (quantity <= 0) {
            throw new BusinessException("Order item quantity must be positive.");
        }
        if (unitPrice == null || unitPrice.signum() <= 0) {
            throw new BusinessException("Order item unit price must be greater than zero.");
        }
        this.order = order;
        this.productId = productId;
        this.productSku = productSku;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    void assignToOrder(Order order) {
        this.order = order;
    }

    void detachFromOrder() {
        this.order = null;
    }

    public BigDecimal lineTotal() {
        return lineTotal;
    }
}