package pl.edu.ecommerceshop.analytics.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.ecommerceshop.analytics.dto.AnalyticsSummaryResponse;
import pl.edu.ecommerceshop.analytics.dto.ProductAnalyticsResponse;
import pl.edu.ecommerceshop.analytics.service.AnalyticsQueryService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/admin/analytics")
@RequiredArgsConstructor
@Tag(name = "Admin Analytics")
public class AnalyticsController {

    private final AnalyticsQueryService analyticsQueryService;

    @Operation(
            summary = "Get analytics summary",
            description = "Returns order, payment and revenue counters stored in Redis."
    )
    @GetMapping("/summary")
    public AnalyticsSummaryResponse getSummary() {
        return analyticsQueryService.getSummary();
    }

    @Operation(
            summary = "Get top viewed products",
            description = "Returns products with the highest number of product detail views."
    )
    @GetMapping("/top-viewed-products")
    public List<ProductAnalyticsResponse> getTopViewedProducts(
            @RequestParam(defaultValue = "10")
            @Min(1)
            @Max(50)
            int limit
    ) {
        return analyticsQueryService.getTopViewedProducts(limit);
    }

    @Operation(
            summary = "Get top cart products",
            description = "Returns products most frequently added to carts."
    )
    @GetMapping("/top-cart-products")
    public List<ProductAnalyticsResponse> getTopCartProducts(
            @RequestParam(defaultValue = "10")
            @Min(1)
            @Max(50)
            int limit
    ) {
        return analyticsQueryService.getTopCartProducts(limit);
    }
}
