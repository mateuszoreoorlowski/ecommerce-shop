package pl.edu.ecommerceshop.analytics.dto;

public record ProductAnalyticsResponse(
        Long productId,
        String sku,
        String productName,
        String imageUrl,
        long score
) {
}
