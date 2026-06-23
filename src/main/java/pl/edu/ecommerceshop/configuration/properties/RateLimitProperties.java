package pl.edu.ecommerceshop.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "shopflow.rate-limiting")
public class RateLimitProperties {

    private boolean enabled = true;

    private String redisKeyPrefix = "shopflow:rate-limit:";

}
