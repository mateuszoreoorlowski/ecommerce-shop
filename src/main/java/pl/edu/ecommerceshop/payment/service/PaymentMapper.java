package pl.edu.ecommerceshop.payment.service;

import pl.edu.ecommerceshop.payment.dto.PaymentResponse;
import pl.edu.ecommerceshop.payment.model.Payment;

public final class PaymentMapper {

    private PaymentMapper() {
    }

    public static PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getOrder().getOrderNumber(),
                payment.getProvider(),
                payment.getStatus(),
                payment.getAmount(),
                payment.getExternalPaymentId(),
                payment.getFailureReason(),
                payment.getCreatedAt(),
                payment.getUpdatedAt()
        );
    }
}
