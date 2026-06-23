package pl.edu.ecommerceshop.analytics;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Log4j2
@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private static final String PRODUCT_VIEWS_KEY = "shopflow:analytics:product-views";
    private static final String CART_ADDS_KEY = "shopflow:analytics:cart-adds";
    private static final String ORDERS_CREATED_KEY = "shopflow:analytics:orders-created";
    private static final String PAYMENTS_COMPLETED_KEY = "shopflow:analytics:payments-completed";
    private static final String PAYMENTS_FAILED_KEY = "shopflow:analytics:payments-failed";
    private static final String PAYMENT_REVENUE_KEY = "shopflow:analytics:payment-revenue";

    private final ObjectProvider<StringRedisTemplate> stringRedisTemplateProvider;

    public void recordProductView(Long productId) {
        execute(redis -> redis.opsForZSet().incrementScore(
                PRODUCT_VIEWS_KEY,
                String.valueOf(productId),
                1
        ));
    }

    public void recordProductAddedToCart(Long productId, int quantity) {
        execute(redis -> redis.opsForZSet().incrementScore(
                CART_ADDS_KEY,
                String.valueOf(productId),
                quantity
        ));
    }

    public void recordOrderCreated() {
        execute(redis -> redis.opsForValue().increment(ORDERS_CREATED_KEY));
    }

    public void recordPaymentCompleted(BigDecimal amount) {
        execute(redis -> {
            redis.opsForValue().increment(PAYMENTS_COMPLETED_KEY);

            if (amount != null) {
                redis.opsForValue().increment(
                        PAYMENT_REVENUE_KEY,
                        amount.doubleValue()
                );
            }
        });
    }

    public void recordPaymentFailed() {
        execute(redis -> redis.opsForValue().increment(PAYMENTS_FAILED_KEY));
    }

    private void execute(RedisOperation operation) {
        StringRedisTemplate redis = stringRedisTemplateProvider.getIfAvailable();

        if (redis == null) {
            return;
        }

        try {
            operation.execute(redis);
        } catch (RuntimeException exception) {
            log.warn("Could not record analytics event.", exception);
        }
    }

    @FunctionalInterface
    private interface RedisOperation {
        void execute(StringRedisTemplate redis);
    }
}