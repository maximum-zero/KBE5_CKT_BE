package kernel360.ckt.gateway.config;

import kernel360.ckt.gateway.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final JwtAuthenticationFilter jwtGatewayFilter;

    @Value("${backend.auth-url}")
    private String authUrl;

    @Value("${backend.admin-url}")
    private String adminUrl;

    @Value("${backend.frontend-origin}")
    private String frontendOrigin;

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()

            .route("auth", r -> r.path("/api/v1/auth/**")
                .uri(authUrl))

            .route("admin", r -> r.path("/api/v1/companies/**")
                .uri(adminUrl))

            .route("customer", r -> r.path("/api/v1/customers/**")
                .uri(adminUrl))

            .route("vehicle", r -> r.path("/api/v1/vehicles/**")
                .uri(adminUrl))

            .route("logs", r -> r.path("/api/v1/logs/**")
                .uri("http://admin:8080"))

            .build();
    }
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(List.of(frontendOrigin));
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}

