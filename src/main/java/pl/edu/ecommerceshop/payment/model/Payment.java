package pl.edu.ecommerceshop.payment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.edu.ecommerceshop.common.exception.BusinessException;
import pl.edu.ecommerceshop.order.model.Order;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private PaymentProvider provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private PaymentStatus status;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(length = 100)
    private String externalPaymentId;

    @Column(length = 500)
    private String failureReason;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected Payment() {
    }

    public Payment(Order order, BigDecimal amount) {
        if (order == null) {
            throw new BusinessException("Payment order cannot be null.");
        }

        if (amount == null || amount.signum() <= 0) {
            throw new BusinessException("Payment amount must be greater than zero.");
        }

        this.order = order;
        this.amount = amount;
        this.provider = PaymentProvider.MOCK;
        this.status = PaymentStatus.PENDING;
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

    public void markPaid(String externalPaymentId) {
        if (status != PaymentStatus.PENDING && status != PaymentStatus.AUTHORIZED) {
            throw new BusinessException("Only pending or authorized payment can be marked as paid.");
        }

        if (externalPaymentId == null || externalPaymentId.isBlank()) {
            throw new BusinessException("External payment id cannot be blank.");
        }

        this.status = PaymentStatus.PAID;
        this.externalPaymentId = externalPaymentId;
        this.failureReason = null;
    }

    public void markFailed(String failureReason) {
        if (status != PaymentStatus.PENDING && status != PaymentStatus.AUTHORIZED) {
            throw new BusinessException("Only pending or authorized payment can be marked as failed.");
        }

        if (failureReason == null || failureReason.isBlank()) {
            throw new BusinessException("Failure reason cannot be blank.");
        }

        this.status = PaymentStatus.FAILED;
        this.failureReason = failureReason;
    }

    public void cancel() {
        if (status != PaymentStatus.PENDING && status != PaymentStatus.AUTHORIZED) {
            throw new BusinessException("Only pending or authorized payment can be cancelled.");
        }

        this.status = PaymentStatus.CANCELLED;
    }

    public void startRefund() {
        if (status != PaymentStatus.PAID) {
            throw new BusinessException("Only paid payment can be refunded.");
        }

        this.status = PaymentStatus.REFUND_PENDING;
    }

    public void markRefunded() {
        if (status != PaymentStatus.REFUND_PENDING && status != PaymentStatus.PAID) {
            throw new BusinessException("Only paid or refund pending payment can be marked as refunded.");
        }

        this.status = PaymentStatus.REFUNDED;
    }
}