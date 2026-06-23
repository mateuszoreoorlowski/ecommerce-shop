package pl.edu.ecommerceshop.analytics.dto;

import java.math.BigDecimal;

public record AnalyticsSummaryResponse(
        long ordersCreated,
        long paymentsCompleted,
        long paymentsFailed,
        BigDecimal paymentRevenue
) {
}
