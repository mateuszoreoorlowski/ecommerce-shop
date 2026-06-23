package pl.edu.ecommerceshop.common.events;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public interface DomainEvent {

    UUID eventId();

    String eventType();

    Instant occurredAt();

    Map<String, String> payload();
}
