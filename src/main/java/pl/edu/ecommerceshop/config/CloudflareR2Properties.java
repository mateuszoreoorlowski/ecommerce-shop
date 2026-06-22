package pl.edu.ecommerceshop.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "shopflow.assets.r2")
public class CloudflareR2Properties {

    private String endpoint;
    private String bucket;
    private String accessKeyId;
    private String secretAccessKey;
    private String productsPrefix = "products";
    private long maxImageSizeBytes = 5 * 1024 * 1024;

    private List<String> allowedContentTypes = List.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

}
