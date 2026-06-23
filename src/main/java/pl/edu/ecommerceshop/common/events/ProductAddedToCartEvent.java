package pl.edu.ecommerceshop.common.events;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record ProductAddedToCartEvent(
        UUID eventId,
        Instant occurredAt,
        Long cartId,
        Long productId,
        int quantity,
        String customerEmail
) implements DomainEvent {

    public ProductAddedToCartEvent(
            Long cartId,
            Long productId,
            int quantity,
            String customerEmail
    ) {
        this(
                UUID.randomUUID(),
                Instant.now(),
                cartId,
                productId,
                quantity,
                customerEmail
        );
    }

    @Override
    public String eventType() {
        return "PRODUCT_ADDED_TO_CART";
    }

    @Override
    public Map<String, String> payload() {
        return Map.of(
                "cartId", String.valueOf(cartId),
                "productId", String.valueOf(productId),
                "quantity", String.valueOf(quantity),
                "customerEmail", customerEmail == null ? "" : customerEmail
        );
    }
}
