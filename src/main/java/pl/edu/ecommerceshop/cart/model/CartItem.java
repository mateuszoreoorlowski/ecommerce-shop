package pl.edu.ecommerceshop.cart.model;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import pl.edu.ecommerceshop.catalog.model.Product;
import pl.edu.ecommerceshop.common.exception.BusinessException;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal unitPriceSnapshot;

    protected CartItem() {
    }

    public CartItem(Cart cart, Product product, int quantity, BigDecimal unitPriceSnapshot) {
        if (quantity <= 0) {
            throw new BusinessException("Cart item quantity must be positive.");
        }
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
        this.unitPriceSnapshot = unitPriceSnapshot;
    }

    public void increaseQuantity(int quantity, BigDecimal currentPrice) {
        if (quantity <= 0) {
            throw new BusinessException("Cart item quantity must be positive.");
        }
        this.quantity += quantity;
        this.unitPriceSnapshot = currentPrice;
    }

    public void changeQuantity(int quantity) {
        if (quantity <= 0) {
            throw new BusinessException("Cart item quantity must be positive.");
        }
        this.quantity = quantity;
    }

    public BigDecimal lineTotal() {
        return unitPriceSnapshot.multiply(BigDecimal.valueOf(quantity));
    }
}
