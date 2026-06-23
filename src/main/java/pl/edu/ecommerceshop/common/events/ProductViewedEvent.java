package pl.edu.ecommerceshop.common.events;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record ProductViewedEvent(
        UUID eventId,
        Instant occurredAt,
        Long productId
) implements DomainEvent {

    public ProductViewedEvent(Long productId) {
        this(UUID.randomUUID(), Instant.now(), productId);
    }

    @Override
    public String eventType() {
        return "PRODUCT_VIEWED";
    }

    @Override
    public Map<String, String> payload() {
        return Map.of(
                "productId", String.valueOf(productId)
        );
    }
}
