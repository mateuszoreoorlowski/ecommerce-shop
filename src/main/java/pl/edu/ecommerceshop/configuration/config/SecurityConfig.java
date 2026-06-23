package pl.edu.ecommerceshop.configuration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/actuator/health", "/actuator/info").permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/auth/register",
                                "/auth/login/start",
                                "/auth/login/verify"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET, "/categories").permitAll()
                        .requestMatchers(HttpMethod.GET, "/products", "/products/**").permitAll()

                        .requestMatchers("/auth/me").authenticated()

                        .requestMatchers(HttpMethod.POST, "/carts").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers("/carts/**").hasAnyRole("CUSTOMER", "ADMIN")

                        .requestMatchers(HttpMethod.POST, "/orders/checkout").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers("/orders", "/orders/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/payments/mock").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers("/payments", "/payments/**").hasRole("ADMIN")

                        .requestMatchers("/categories", "/categories/**").hasRole("ADMIN")
                        .requestMatchers("/products", "/products/**").hasRole("ADMIN")
                        .requestMatchers("/stock-movements", "/stock-movements/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/products/with-image").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/products/*/with-image").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = jwt -> {
            List<String> roles = jwt.getClaimAsStringList("roles");
            if (roles == null) {
                return List.of();
            }

            return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .map(GrantedAuthority.class::cast)
                    .toList();
        };

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        converter.setPrincipalClaimName("email");
        return converter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}