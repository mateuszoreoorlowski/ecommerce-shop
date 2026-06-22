package pl.edu.ecommerceshop.catalog.model;

import jakarta.persistence.*;
import lombok.Getter;
import pl.edu.ecommerceshop.common.exception.BusinessException;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String sku;

    @Column(nullable = false, length = 180)
    private String name;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(length = 2048)
    private String imageUrl;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private int stockQuantity;

    @Column(nullable = false)
    private int reservedQuantity;

    @Column(nullable = false)
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Version
    @Column(nullable = false)
    private Long version;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected Product() {
    }

    public Product(String sku, String name, String description, String imageUrl, BigDecimal price, int stockQuantity, Category category ) {
        if (stockQuantity < 0) {
            throw new BusinessException("Initial stock cannot be negative.");
        }

        if (category == null) {
            throw new BusinessException("Product category cannot be null.");
        }

        this.sku = sku;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.reservedQuantity = 0;
        this.active = true;
        this.category = category;
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

    public void updateDetails(String name, String description, String imageUrl, BigDecimal price, Boolean active, Category category) {
        if (name != null) {
            this.name = name;
        }

        if (description != null) {
            this.description = description;
        }

        if (imageUrl != null) {
            this.imageUrl = imageUrl.isBlank() ? null : imageUrl;
        }

        if (price != null) {
            this.price = price;
        }

        if (active != null) {
            this.active = active;
        }

        if (category != null) {
            this.category = category;
        }
    }

    public void receiveStock(int quantity) {
        if (quantity <= 0) {
            throw new BusinessException("Received stock quantity must be positive.");
        }
        this.stockQuantity += quantity;
    }

    public void restoreStockAfterCancellationOrReturn(int quantity) {
        receiveStock(quantity);
    }

    public void reserve(int quantity) {
        if (!active) {
            throw new BusinessException("Product %s is not active.".formatted(sku));
        }
        if (quantity <= 0) {
            throw new BusinessException("Reserved quantity must be positive.");
        }
        if (availableQuantity() < quantity) {
            throw new BusinessException("Not enough stock for product %s. Available: %d, requested: %d."
                    .formatted(sku, availableQuantity(), quantity));
        }
        this.reservedQuantity += quantity;
    }

    public void releaseReservation(int quantity) {
        if (quantity <= 0) {
            throw new BusinessException("Released quantity must be positive.");
        }
        if (reservedQuantity < quantity) {
            throw new BusinessException("Cannot release more items than reserved for product %s.".formatted(sku));
        }
        this.reservedQuantity -= quantity;
    }

    public void commitReservedSale(int quantity) {
        if (quantity <= 0) {
            throw new BusinessException("Sale quantity must be positive.");
        }
        if (reservedQuantity < quantity) {
            throw new BusinessException("Cannot commit sale without reservation for product %s.".formatted(sku));
        }
        if (stockQuantity < quantity) {
            throw new BusinessException("Cannot commit sale because stock is lower than requested quantity for product %s.".formatted(sku));
        }
        this.reservedQuantity -= quantity;
        this.stockQuantity -= quantity;
    }

    public int availableQuantity() {
        return stockQuantity - reservedQuantity;
    }
}