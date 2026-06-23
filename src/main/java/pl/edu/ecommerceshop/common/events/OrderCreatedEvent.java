package pl.edu.ecommerceshop.common.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record OrderCreatedEvent(
        UUID eventId,
        Instant occurredAt,
        Long orderId,
        String orderNumber,
        String customerEmail,
        BigDecimal totalPrice
) implements DomainEvent {

    public OrderCreatedEvent(
            Long orderId,
            String orderNumber,
            String customerEmail,
            BigDecimal totalPrice
    ) {
        this(
                UUID.randomUUID(),
                Instant.now(),
                orderId,
                orderNumber,
                customerEmail,
                totalPrice
        );
    }

    @Override
    public String eventType() {
        return "ORDER_CREATED";
    }

    @Override
    public Map<String, String> payload() {
        return Map.of(
                "orderId", String.valueOf(orderId),
                "orderNumber", orderNumber == null ? "" : orderNumber,
                "customerEmail", customerEmail == null ? "" : customerEmail,
                "totalPrice", totalPrice == null ? "0" : totalPrice.toPlainString()
        );
    }
}
