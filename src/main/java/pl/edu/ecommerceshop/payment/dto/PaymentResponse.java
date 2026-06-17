package pl.edu.ecommerceshop.payment.dto;

import pl.edu.ecommerceshop.payment.model.PaymentProvider;
import pl.edu.ecommerceshop.payment.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentResponse(
        Long id,
        Long orderId,
        String orderNumber,
        PaymentProvider provider,
        PaymentStatus status,
        BigDecimal amount,
        String externalPaymentId,
        String failureReason,
        Instant createdAt,
        Instant updatedAt
) {
}
