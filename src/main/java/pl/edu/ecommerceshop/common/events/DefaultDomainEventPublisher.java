package pl.edu.ecommerceshop.common.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import pl.edu.ecommerceshop.configuration.properties.EventsProperties;

import java.util.LinkedHashMap;
import java.util.Map;

@Log4j2
@Component
@RequiredArgsConstructor
public class DefaultDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final StringRedisTemplate stringRedisTemplate;
    private final EventsProperties eventsProperties;

    @Override
    public void publish(DomainEvent event) {
        if (event == null) {
            return;
        }

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            publishNow(event);
                        }
                    }
            );

            return;
        }

        publishNow(event);
    }

    private void publishNow(DomainEvent event) {
        publishSpringEvent(event);
        publishRedisStreamEvent(event);
    }

    private void publishSpringEvent(DomainEvent event) {
        try {
            applicationEventPublisher.publishEvent(event);
        } catch (RuntimeException exception) {
            log.warn(
                    "Could not publish Spring domain event. Event type: {}, event id: {}",
                    event.eventType(),
                    event.eventId(),
                    exception
            );
        }
    }

    private void publishRedisStreamEvent(DomainEvent event) {
        if (!eventsProperties.getRedisStream().isEnabled()) {
            return;
        }

        try {
            Map<String, String> streamPayload = new LinkedHashMap<>();

            streamPayload.put("eventId", event.eventId().toString());
            streamPayload.put("eventType", event.eventType());
            streamPayload.put("occurredAt", event.occurredAt().toString());
            streamPayload.putAll(event.payload());

            MapRecord<String, String, String> record = MapRecord.create(
                    eventsProperties.getRedisStream().getKey(),
                    streamPayload
            );

            stringRedisTemplate.opsForStream().add(record);
        } catch (RuntimeException exception) {
            log.warn(
                    "Could not publish domain event to Redis Stream. Event type: {}, event id: {}",
                    event.eventType(),
                    event.eventId(),
                    exception
            );
        }
    }
}
