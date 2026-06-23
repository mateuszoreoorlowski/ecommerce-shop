package pl.edu.ecommerceshop.analytics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import pl.edu.ecommerceshop.analytics.dto.AnalyticsSummaryResponse;
import pl.edu.ecommerceshop.analytics.dto.ProductAnalyticsResponse;
import pl.edu.ecommerceshop.analytics.helper.AnalyticsKeys;
import pl.edu.ecommerceshop.catalog.assets.ProductAssetUrlResolver;
import pl.edu.ecommerceshop.catalog.model.Product;
import pl.edu.ecommerceshop.catalog.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsQueryServiceImpl implements AnalyticsQueryService {

    private static final int DEFAULT_LIMIT = 10;
    private static final int MAX_LIMIT = 50;

    private final ObjectProvider<StringRedisTemplate> stringRedisTemplateProvider;
    private final ProductRepository productRepository;
    private final ProductAssetUrlResolver productAssetUrlResolver;

    @Override
    public AnalyticsSummaryResponse getSummary() {
        StringRedisTemplate redis = stringRedisTemplateProvider.getIfAvailable();

        if (redis == null) {
            return new AnalyticsSummaryResponse(
                    0,
                    0,
                    0,
                    BigDecimal.ZERO
            );
        }

        return new AnalyticsSummaryResponse(
                readLong(redis, AnalyticsKeys.ORDERS_CREATED),
                readLong(redis, AnalyticsKeys.PAYMENTS_COMPLETED),
                readLong(redis, AnalyticsKeys.PAYMENTS_FAILED),
                readBigDecimal(redis, AnalyticsKeys.PAYMENT_REVENUE)
        );
    }

    @Override
    public List<ProductAnalyticsResponse> getTopViewedProducts(int limit) {
        return getTopProducts(
                AnalyticsKeys.PRODUCT_VIEWS,
                normalizeLimit(limit)
        );
    }

    @Override
    public List<ProductAnalyticsResponse> getTopCartProducts(int limit) {
        return getTopProducts(
                AnalyticsKeys.CART_ADDS,
                normalizeLimit(limit)
        );
    }

    private List<ProductAnalyticsResponse> getTopProducts(String redisKey, int limit) {
        StringRedisTemplate redis = stringRedisTemplateProvider.getIfAvailable();

        if (redis == null) {
            return List.of();
        }

        Set<ZSetOperations.TypedTuple<String>> tuples =
                redis.opsForZSet().reverseRangeWithScores(redisKey, 0, limit - 1);

        if (tuples == null || tuples.isEmpty()) {
            return List.of();
        }

        List<ProductScore> productScores = tuples.stream()
                .map(this::mapTupleToProductScore)
                .flatMap(Optional::stream)
                .sorted(Comparator.comparing(ProductScore::score).reversed())
                .toList();

        if (productScores.isEmpty()) {
            return List.of();
        }

        List<Long> productIds = productScores.stream()
                .map(ProductScore::productId)
                .toList();

        Map<Long, Product> productsById = productRepository.findAllById(productIds)
                .stream()
                .collect(Collectors.toMap(
                        Product::getId,
                        Function.identity()
                ));

        return productScores.stream()
                .map(productScore -> mapToProductAnalyticsResponse(
                        productScore,
                        productsById.get(productScore.productId())
                ))
                .toList();
    }

    private Optional<ProductScore> mapTupleToProductScore(
            ZSetOperations.TypedTuple<String> tuple
    ) {
        if (tuple == null || tuple.getValue() == null || tuple.getScore() == null) {
            return Optional.empty();
        }

        try {
            Long productId = Long.valueOf(tuple.getValue());
            long score = Math.round(tuple.getScore());

            return Optional.of(new ProductScore(productId, score));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    private ProductAnalyticsResponse mapToProductAnalyticsResponse(
            ProductScore productScore,
            Product product
    ) {
        if (product == null) {
            return new ProductAnalyticsResponse(
                    productScore.productId(),
                    null,
                    "Unknown product",
                    null,
                    productScore.score()
            );
        }

        return new ProductAnalyticsResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                productAssetUrlResolver.normalizeImageUrl(product.getImageUrl()),
                productScore.score()
        );
    }

    private long readLong(StringRedisTemplate redis, String key) {
        String value = redis.opsForValue().get(key);

        if (value == null || value.isBlank()) {
            return 0;
        }

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

    private BigDecimal readBigDecimal(StringRedisTemplate redis, String key) {
        String value = redis.opsForValue().get(key);

        if (value == null || value.isBlank()) {
            return BigDecimal.ZERO;
        }

        try {
            return new BigDecimal(value);
        } catch (NumberFormatException exception) {
            return BigDecimal.ZERO;
        }
    }

    private int normalizeLimit(int limit) {
        if (limit <= 0) {
            return DEFAULT_LIMIT;
        }

        return Math.min(limit, MAX_LIMIT);
    }

    private record ProductScore(
            Long productId,
            long score
    ) {
    }
}
