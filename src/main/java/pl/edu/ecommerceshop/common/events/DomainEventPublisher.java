package pl.edu.ecommerceshop.common.events;

public interface DomainEventPublisher {

    void publish(DomainEvent event);
}
