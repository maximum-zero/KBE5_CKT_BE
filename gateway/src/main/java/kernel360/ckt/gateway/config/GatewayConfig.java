package kernel360.ckt.gateway.config;

import kernel360.ckt.gateway.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final JwtAuthenticationFilter jwtGatewayFilter;

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()

            .route("auth", r -> r.path("/api/v1/auth/**")
                .uri("http://auth:8082"))

            .route("admin", r -> r.path("/api/v1/companies/**")
            .uri("http://admin:8080"))

            .route("customer", r -> r.path("/api/v1/customers/**")
                .uri("http://admin:8080"))

            .route("vehicle", r -> r.path("/api/v1/vehicles/**")
                .uri("http://admin:8080"))

            .route("driving-log", r -> r.path("/api/v1/logs/drive/**")
                .uri("http://admin:8080"))

            .build();
    }
}
