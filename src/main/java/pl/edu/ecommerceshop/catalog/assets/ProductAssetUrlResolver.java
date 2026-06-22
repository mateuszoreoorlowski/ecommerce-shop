package pl.edu.ecommerceshop.catalog.assets;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.ecommerceshop.config.AssetsProperties;

@Component
@RequiredArgsConstructor
public class ProductAssetUrlResolver {

    private final AssetsProperties assetsProperties;

    public String normalizeImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return null;
        }

        String normalized = imageUrl.trim();

        if (isAbsoluteUrl(normalized)) {
            return normalized;
        }

        String baseUrl = normalizeBaseUrl(assetsProperties.getCdnBaseUrl());

        if (baseUrl == null) {
            return normalized.startsWith("/") ? normalized : "/" + normalized;
        }

        String path = normalized.startsWith("/") ? normalized : "/" + normalized;

        return baseUrl + path;
    }

    private boolean isAbsoluteUrl(String value) {
        return value.startsWith("http://") || value.startsWith("https://");
    }

    private String normalizeBaseUrl(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        String normalized = value.trim();

        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }

        return normalized;
    }
}
