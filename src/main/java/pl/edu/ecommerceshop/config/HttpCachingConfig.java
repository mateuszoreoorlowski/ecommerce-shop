package pl.edu.ecommerceshop.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@Configuration
public class HttpCachingConfig {

    @Bean
    public FilterRegistrationBean<ShallowEtagHeaderFilter> shallowEtagHeaderFilter() {
        FilterRegistrationBean<ShallowEtagHeaderFilter> registration =
                new FilterRegistrationBean<>(new ShallowEtagHeaderFilter());

        registration.addUrlPatterns(
                "/products",
                "/products/*",
                "/categories",
                "/categories/*"
        );

        registration.setName("shallowEtagHeaderFilter");

        return registration;
    }
}
