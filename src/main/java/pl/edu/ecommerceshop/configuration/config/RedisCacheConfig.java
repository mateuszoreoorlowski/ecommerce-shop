package pl.edu.ecommerceshop.configuration.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
@EnableCaching
public class RedisCacheConfig implements CachingConfigurer {

    public static final String CATEGORIES_CACHE = "categories";
    public static final String PRODUCT_CACHE = "product";
    public static final String PRODUCTS_CACHE = "products";

    private static final String CACHE_PREFIX = "shopflow:v2:";

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration defaultConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .prefixCacheNameWith(CACHE_PREFIX)
                .disableCachingNullValues()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json())
                );

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultConfiguration)
                .withCacheConfiguration(CATEGORIES_CACHE, defaultConfiguration.entryTtl(Duration.ofMinutes(30)))
                .withCacheConfiguration(PRODUCT_CACHE, defaultConfiguration.entryTtl(Duration.ofMinutes(10)))
                .withCacheConfiguration(PRODUCTS_CACHE, defaultConfiguration.entryTtl(Duration.ofMinutes(5)))
                .build();
    }

    @Bean("catalogCacheKeyGenerator")
    @Override
    public KeyGenerator keyGenerator() {
        return (_, method, params) -> method.getName() + ":" + Arrays.stream(params)
                .map(this::normalizeCacheKeyPart)
                .collect(Collectors.joining("|"));
    }

    private String normalizeCacheKeyPart(Object param) {
        if (param == null) {
            return "null";
        }

        if (param instanceof Pageable pageable) {
            return "page=%d,size=%d,sort=%s".formatted(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    pageable.getSort()
            );
        }

        return param.toString();
    }
}