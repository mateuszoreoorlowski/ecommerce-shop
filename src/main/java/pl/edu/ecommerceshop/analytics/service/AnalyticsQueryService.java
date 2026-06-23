package pl.edu.ecommerceshop.analytics.service;

import pl.edu.ecommerceshop.analytics.dto.AnalyticsSummaryResponse;
import pl.edu.ecommerceshop.analytics.dto.ProductAnalyticsResponse;

import java.util.List;

public interface AnalyticsQueryService {

    AnalyticsSummaryResponse getSummary();

    List<ProductAnalyticsResponse> getTopViewedProducts(int limit);

    List<ProductAnalyticsResponse> getTopCartProducts(int limit);
}
