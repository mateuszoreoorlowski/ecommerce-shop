package pl.edu.ecommerceshop.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Order(Ordered.LOWEST_PRECEDENCE - 20)
public class PublicCatalogCacheFilter extends OncePerRequestFilter {

    private static final String PUBLIC_CATALOG_CACHE_VALUE = CacheControl
            .maxAge(60, TimeUnit.SECONDS)
            .cachePublic()
            .getHeaderValue();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !isPublicCatalogReadRequest(request);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        filterChain.doFilter(request, response);

        if (response.getStatus() >= 200 && response.getStatus() < 300) {
            response.setHeader(HttpHeaders.CACHE_CONTROL, PUBLIC_CATALOG_CACHE_VALUE);
        }
    }

    private boolean isPublicCatalogReadRequest(HttpServletRequest request) {
        String method = request.getMethod();

        if (!HttpMethod.GET.name().equalsIgnoreCase(method)
                && !HttpMethod.HEAD.name().equalsIgnoreCase(method)) {
            return false;
        }

        String path = request.getRequestURI();

        return path.equals("/products")
                || path.startsWith("/products/")
                || path.equals("/categories")
                || path.startsWith("/categories/");
    }
}
