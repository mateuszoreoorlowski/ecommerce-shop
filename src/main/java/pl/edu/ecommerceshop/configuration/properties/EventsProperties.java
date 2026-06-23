package pl.edu.ecommerceshop.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "shopflow.events")
public class EventsProperties {

    private final RedisStream redisStream = new RedisStream();

    @Setter
    @Getter
    public static class RedisStream {

        private boolean enabled = true;

        private String key = "shopflow:events";

    }
}
