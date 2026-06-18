package pl.edu.ecommerceshop.cart.model;

import jakarta.persistence.*;
import lombok.Getter;
import pl.edu.ecommerceshop.catalog.model.Product;
import pl.edu.ecommerceshop.common.exception.BusinessException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 180)
    private String customerEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CartStatus status;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected Cart() {
    }

    public Cart(String customerEmail) {
        if (customerEmail == null || customerEmail.isBlank()) {
            throw new BusinessException("Customer email cannot be blank.");
        }

        this.customerEmail = customerEmail.trim().toLowerCase();
        this.status = CartStatus.ACTIVE;
    }

    @PrePersist
    void prePersist() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = Instant.now();
    }

    public void addItem(Product product, int quantity) {
        ensureActive();
        if (product == null) {
            throw new BusinessException("Product cannot be null.");
        }
        if (quantity <= 0) {
            throw new BusinessException("Cart item quantity must be positive.");
        }
        Optional<CartItem> existingItem = items.stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();
        if (existingItem.isPresent()) {
            existingItem.get().increaseQuantity(quantity, product.getPrice());
        } else {
            items.add(new CartItem(this, product, quantity, product.getPrice()));
        }
    }

    public void updateItemQuantity(Long itemId, int quantity) {
        ensureActive();
        CartItem item = items.stream()
                .filter(cartItem -> cartItem.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Cart item not found in this cart."));
        item.changeQuantity(quantity);
    }

    public void removeItem(Long itemId) {
        ensureActive();
        CartItem item = items.stream()
                .filter(cartItem -> cartItem.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Cart item not found in this cart."));
        items.remove(item);
        item.detachFromCart();
    }

    public void markOrdered() {
        ensureActive();
        if (items.isEmpty()) {
            throw new BusinessException("Cannot checkout an empty cart.");
        }
        this.status = CartStatus.ORDERED;
    }

    private void ensureActive() {
        if (status != CartStatus.ACTIVE) {
            throw new BusinessException("Cart is not active.");
        }
    }
}