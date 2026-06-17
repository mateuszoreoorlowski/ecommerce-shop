package pl.edu.ecommerceshop.order.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.edu.ecommerceshop.common.exception.BusinessException;
import pl.edu.ecommerceshop.payment.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String orderNumber;

    @Column(nullable = false, length = 160)
    private String customerName;

    @Column(nullable = false, length = 180)
    private String customerEmail;

    @Column(nullable = false, length = 40)
    private String customerPhone;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "delivery_street", nullable = false, length = 180)),
            @AttributeOverride(name = "city", column = @Column(name = "delivery_city", nullable = false, length = 120)),
            @AttributeOverride(name = "postalCode", column = @Column(name = "delivery_postal_code", nullable = false, length = 40)),
            @AttributeOverride(name = "country", column = @Column(name = "delivery_country", nullable = false, length = 80))
    })
    private Address deliveryAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private PaymentStatus paymentStatus;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    private Instant paidAt;

    private Instant cancelledAt;

    protected Order() {
    }

    public Order(
            String orderNumber,
            String customerName,
            String customerEmail,
            String customerPhone,
            Address deliveryAddress
    ) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.deliveryAddress = deliveryAddress;
        this.status = OrderStatus.NEW;
        this.paymentStatus = PaymentStatus.PENDING;
        this.totalPrice = BigDecimal.ZERO;
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

    public void addItem(OrderItem item) {
        if (item == null) {
            throw new BusinessException("Order item cannot be null.");
        }

        items.add(item);
        item.setOrder(this);
        recalculateTotal();
    }

    public void removeItem(OrderItem item) {
        if (item == null) {
            return;
        }

        items.remove(item);
        item.setOrder(null);
        recalculateTotal();
    }

    public void markPaymentPaid() {
        if (status != OrderStatus.NEW) {
            throw new BusinessException("Only new orders can be marked as paid.");
        }

        if (paymentStatus == PaymentStatus.PAID) {
            throw new BusinessException("Order is already paid.");
        }

        if (paymentStatus == PaymentStatus.FAILED || paymentStatus == PaymentStatus.CANCELLED) {
            throw new BusinessException("Failed or cancelled payment cannot be marked as paid.");
        }

        this.paymentStatus = PaymentStatus.PAID;
        this.status = OrderStatus.CONFIRMED;
        this.paidAt = Instant.now();
    }

    public void markPaymentFailed() {
        if (status != OrderStatus.NEW) {
            throw new BusinessException("Only new orders can fail payment.");
        }

        if (paymentStatus == PaymentStatus.PAID) {
            throw new BusinessException("Paid order cannot be marked as payment failed.");
        }

        this.paymentStatus = PaymentStatus.FAILED;
    }

    public void cancelAfterFailedPayment() {
        markPaymentFailed();

        this.status = OrderStatus.CANCELLED;
        this.cancelledAt = Instant.now();
    }

    public void cancel() {
        if (isClosed()) {
            throw new BusinessException("Closed order cannot be cancelled.");
        }

        if (status == OrderStatus.SHIPPED || status == OrderStatus.DELIVERED || status == OrderStatus.COMPLETED) {
            throw new BusinessException("Shipped, delivered or completed order cannot be cancelled directly.");
        }

        this.status = OrderStatus.CANCELLED;
        this.cancelledAt = Instant.now();

        if (paymentStatus == PaymentStatus.PENDING || paymentStatus == PaymentStatus.FAILED) {
            this.paymentStatus = PaymentStatus.CANCELLED;
        }
    }

    public void changeStatus(OrderStatus newStatus) {
        if (newStatus == null) {
            throw new BusinessException("Order status cannot be null.");
        }

        if (newStatus == OrderStatus.NEW) {
            throw new BusinessException("Order cannot be changed back to NEW.");
        }

        if (newStatus == OrderStatus.CANCELLED) {
            cancel();
            return;
        }

        if (newStatus == OrderStatus.RETURN_REQUESTED) {
            startReturn();
            return;
        }

        if (newStatus == OrderStatus.RETURNED) {
            markReturned();
            return;
        }

        if (newStatus == OrderStatus.REFUNDED) {
            markRefunded();
            return;
        }

        if (isClosed()) {
            throw new BusinessException("Closed order cannot change status.");
        }

        if (paymentStatus != PaymentStatus.PAID && requiresPaidOrder(newStatus)) {
            throw new BusinessException("Order must be paid before fulfilment status can be changed.");
        }

        if (!allowedNextStatuses().contains(newStatus)) {
            throw new BusinessException("Cannot change order status from %s to %s.".formatted(status, newStatus));
        }

        this.status = newStatus;
    }

    public void startReturn() {
        if (status != OrderStatus.DELIVERED && status != OrderStatus.COMPLETED) {
            throw new BusinessException("Only delivered or completed orders can be returned.");
        }

        this.status = OrderStatus.RETURN_REQUESTED;
    }

    public void markReturned() {
        if (status != OrderStatus.RETURN_REQUESTED) {
            throw new BusinessException("Only return requested orders can be marked as returned.");
        }

        this.status = OrderStatus.RETURNED;
    }

    public void markRefunded() {
        if (status != OrderStatus.RETURNED && status != OrderStatus.CANCELLED) {
            throw new BusinessException("Only returned or cancelled orders can be marked as refunded.");
        }

        this.paymentStatus = PaymentStatus.REFUNDED;
        this.status = OrderStatus.REFUNDED;
    }

    private boolean requiresPaidOrder(OrderStatus newStatus) {
        return switch (newStatus) {
            case CONFIRMED,
                 PROCESSING,
                 READY_TO_SHIP,
                 SHIPPED,
                 DELIVERED,
                 COMPLETED,
                 RETURN_REQUESTED,
                 RETURNED,
                 REFUNDED -> true;
            case NEW,
                 CANCELLED -> false;
        };
    }

    private Set<OrderStatus> allowedNextStatuses() {
        return switch (status) {
            case NEW -> EnumSet.of(OrderStatus.CONFIRMED, OrderStatus.CANCELLED);
            case CONFIRMED -> EnumSet.of(OrderStatus.PROCESSING, OrderStatus.CANCELLED);
            case PROCESSING -> EnumSet.of(OrderStatus.READY_TO_SHIP, OrderStatus.CANCELLED);
            case READY_TO_SHIP -> EnumSet.of(OrderStatus.SHIPPED, OrderStatus.CANCELLED);
            case SHIPPED -> EnumSet.of(OrderStatus.DELIVERED);
            case DELIVERED -> EnumSet.of(OrderStatus.COMPLETED, OrderStatus.RETURN_REQUESTED);
            case COMPLETED -> EnumSet.of(OrderStatus.RETURN_REQUESTED);
            case RETURN_REQUESTED -> EnumSet.of(OrderStatus.RETURNED);
            case RETURNED -> EnumSet.of(OrderStatus.REFUNDED);
            case CANCELLED, REFUNDED -> EnumSet.noneOf(OrderStatus.class);
        };
    }

    private boolean isClosed() {
        return status == OrderStatus.CANCELLED || status == OrderStatus.REFUNDED;
    }

    private void recalculateTotal() {
        this.totalPrice = items.stream()
                .map(OrderItem::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}