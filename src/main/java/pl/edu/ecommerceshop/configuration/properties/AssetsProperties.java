package pl.edu.ecommerceshop.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "shopflow.assets")
public class AssetsProperties {

    private String cdnBaseUrl;

}
