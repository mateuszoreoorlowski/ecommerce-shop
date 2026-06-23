package pl.edu.ecommerceshop.common.ratelimit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.edu.ecommerceshop.config.RateLimitProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;

@Log4j2
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 20)
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private static final List<RateLimitRule> RULES = List.of(
            new RateLimitRule(
                    "auth-register",
                    "/auth/register",
                    HttpMethod.POST,
                    5,
                    Duration.ofMinutes(1)
            ),
            new RateLimitRule(
                    "auth-login-start",
                    "/auth/login/start",
                    HttpMethod.POST,
                    5,
                    Duration.ofMinutes(1)
            ),
            new RateLimitRule(
                    "auth-login-verify",
                    "/auth/login/verify",
                    HttpMethod.POST,
                    5,
                    Duration.ofMinutes(1)
            ),

            new RateLimitRule(
                    "payments-create",
                    "/payments/**",
                    HttpMethod.POST,
                    10,
                    Duration.ofMinutes(1)
            ),

            new RateLimitRule(
                    "carts",
                    "/carts/**",
                    null,
                    60,
                    Duration.ofMinutes(1)
            ),

            new RateLimitRule(
                    "products-read",
                    "/products/**",
                    HttpMethod.GET,
                    300,
                    Duration.ofMinutes(1)
            ),
            new RateLimitRule(
                    "categories-read",
                    "/categories/**",
                    HttpMethod.GET,
                    300,
                    Duration.ofMinutes(1)
            ),

            new RateLimitRule(
                    "products-create",
                    "/products",
                    HttpMethod.POST,
                    30,
                    Duration.ofMinutes(1)
            ),
            new RateLimitRule(
                    "products-create-with-image",
                    "/products/with-image",
                    HttpMethod.POST,
                    10,
                    Duration.ofMinutes(5)
            ),
            new RateLimitRule(
                    "products-update",
                    "/products/*",
                    HttpMethod.PATCH,
                    60,
                    Duration.ofMinutes(1)
            ),
            new RateLimitRule(
                    "products-update-stock",
                    "/products/*/stock",
                    HttpMethod.PATCH,
                    60,
                    Duration.ofMinutes(1)
            ),
            new RateLimitRule(
                    "products-update-with-image",
                    "/products/*/with-image",
                    HttpMethod.PATCH,
                    20,
                    Duration.ofMinutes(5)
            )
    );

    private final StringRedisTemplate stringRedisTemplate;
    private final RateLimitProperties properties;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !properties.isEnabled()
                || HttpMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod())
                || findRule(request).isEmpty();
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        RateLimitRule rule = findRule(request).orElseThrow();
        String key = buildKey(request, rule);

        try {
            Long current = stringRedisTemplate.opsForValue().increment(key);

            if (current != null && current == 1L) {
                stringRedisTemplate.expire(key, rule.window());
            }

            if (current != null && current > rule.limit()) {
                writeTooManyRequestsResponse(response, rule);
                return;
            }
        } catch (RuntimeException exception) {
            log.warn(
                    "Rate limiter failed. Request will be allowed. Path: {}",
                    request.getRequestURI(),
                    exception
            );
        }

        filterChain.doFilter(request, response);
    }

    private Optional<RateLimitRule> findRule(HttpServletRequest request) {
        String path = requestPathWithoutContextPath(request);
        String method = request.getMethod();

        return RULES.stream()
                .filter(rule -> rule.matches(path, method))
                .findFirst();
    }

    private String requestPathWithoutContextPath(HttpServletRequest request) {
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();

        if (contextPath != null && !contextPath.isBlank() && path.startsWith(contextPath)) {
            return path.substring(contextPath.length());
        }

        return path;
    }

    private String buildKey(HttpServletRequest request, RateLimitRule rule) {
        return properties.getRedisKeyPrefix()
                + rule.name()
                + ":"
                + clientIdentity(request);
    }

    private String clientIdentity(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization != null && !authorization.isBlank()) {
            return "token:" + sha256(authorization);
        }

        return "ip:" + clientIp(request);
    }

    private String clientIp(HttpServletRequest request) {
        String cloudflareIp = request.getHeader("CF-Connecting-IP");

        if (cloudflareIp != null && !cloudflareIp.isBlank()) {
            return cloudflareIp.trim();
        }

        String realIp = request.getHeader("X-Real-IP");

        if (realIp != null && !realIp.isBlank()) {
            return realIp.trim();
        }

        String forwardedFor = request.getHeader("X-Forwarded-For");

        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }

    private void writeTooManyRequestsResponse(
            HttpServletResponse response,
            RateLimitRule rule
    ) throws IOException {
        long retryAfterSeconds = Math.max(1, rule.window().toSeconds());

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setHeader(HttpHeaders.RETRY_AFTER, String.valueOf(retryAfterSeconds));
        response.setHeader("X-RateLimit-Limit", String.valueOf(rule.limit()));
        response.setHeader("X-RateLimit-Window-Seconds", String.valueOf(retryAfterSeconds));
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/problem+json");

        response.getWriter().write("""
                {
                  "title": "Too many requests",
                  "status": 429,
                  "detail": "Rate limit exceeded. Try again later."
                }
                """);
    }

    private String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            return HexFormat.of().formatHex(
                    digest.digest(value.getBytes(StandardCharsets.UTF_8))
            );
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 digest is not available.", exception);
        }
    }
}