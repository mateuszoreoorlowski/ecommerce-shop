package pl.edu.ecommerceshop.common.ratelimit;

import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;

import java.time.Duration;

public record RateLimitRule(
        String name,
        String pattern,
        HttpMethod method,
        int limit,
        Duration window
) {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    public boolean matches(String path, String requestMethod) {
        if (method != null && !method.name().equalsIgnoreCase(requestMethod)) {
            return false;
        }

        if (pattern.endsWith("/**")) {
            String prefix = pattern.substring(0, pattern.length() - 3);

            if (path.equals(prefix) || path.startsWith(prefix + "/")) {
                return true;
            }
        }

        return PATH_MATCHER.match(pattern, path);
    }
}
