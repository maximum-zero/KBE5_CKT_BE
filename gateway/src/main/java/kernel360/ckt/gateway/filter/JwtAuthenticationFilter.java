package kernel360.ckt.gateway.filter;

import kernel360.ckt.gateway.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;

import java.util.List;

@Component
@RequiredArgsConstructor
@Order(0)
public class JwtAuthenticationFilter implements GlobalFilter {

    private final JwtTokenProvider jwtTokenProvider;

    // 인증 없이 통과할 경로 목록
    private static final List<String> WHITELIST = List.of(
        "/api/v1/auth/login",
        "/api/v1/auth/join",
        "/api/v1/auth/password-reset"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();

        // whitelist에 포함된 경로는 필터 통과
        if (WHITELIST.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }

        // token 헤더 가져오기
        String token = exchange.getRequest().getHeaders().getFirst("Token");

        // 유효하지 않으면 401 반환
        if (token == null || !jwtTokenProvider.validationToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }
}
