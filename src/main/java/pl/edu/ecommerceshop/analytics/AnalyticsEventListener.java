package pl.edu.ecommerceshop.analytics;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pl.edu.ecommerceshop.common.events.*;

@Component
@RequiredArgsConstructor
public class AnalyticsEventListener {

    private final AnalyticsService analyticsService;

    @Async("applicationTaskExecutor")
    @EventListener
    public void onProductViewed(ProductViewedEvent event) {
        analyticsService.recordProductView(event.productId());
    }

    @Async("applicationTaskExecutor")
    @EventListener
    public void onProductAddedToCart(ProductAddedToCartEvent event) {
        analyticsService.recordProductAddedToCart(
                event.productId(),
                event.quantity()
        );
    }

    @Async("applicationTaskExecutor")
    @EventListener
    public void onOrderCreated(OrderCreatedEvent event) {
        analyticsService.recordOrderCreated();
    }

    @Async("applicationTaskExecutor")
    @EventListener
    public void onPaymentCompleted(PaymentCompletedEvent event) {
        analyticsService.recordPaymentCompleted(event.amount());
    }

    @Async("applicationTaskExecutor")
    @EventListener
    public void onPaymentFailed(PaymentFailedEvent event) {
        analyticsService.recordPaymentFailed();
    }
}
