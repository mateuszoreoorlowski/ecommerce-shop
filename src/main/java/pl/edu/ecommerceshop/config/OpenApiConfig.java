package pl.edu.ecommerceshop.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI shopflowOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("ShopFlow E-commerce API")
                        .version("0.1.0")
                        .description("Backend e-commerce: products, carts, checkout, inventory reservation, orders and mock payments.")
                        .contact(new Contact().name("Mateusz Orłowski")));
    }
}