package pl.edu.ecommerceshop.common.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record PaymentCompletedEvent(
        UUID eventId,
        Instant occurredAt,
        Long orderId,
        String orderNumber,
        String customerEmail,
        BigDecimal amount
) implements DomainEvent {

    public PaymentCompletedEvent(
            Long orderId,
            String orderNumber,
            String customerEmail,
            BigDecimal amount
    ) {
        this(
                UUID.randomUUID(),
                Instant.now(),
                orderId,
                orderNumber,
                customerEmail,
                amount
        );
    }

    @Override
    public String eventType() {
        return "PAYMENT_COMPLETED";
    }

    @Override
    public Map<String, String> payload() {
        return Map.of(
                "orderId", String.valueOf(orderId),
                "orderNumber", orderNumber == null ? "" : orderNumber,
                "customerEmail", customerEmail == null ? "" : customerEmail,
                "amount", amount == null ? "0" : amount.toPlainString()
        );
    }
}
