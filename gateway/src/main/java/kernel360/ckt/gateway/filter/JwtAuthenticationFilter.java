package kernel360.ckt.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import kernel360.ckt.core.common.response.ErrorResponse;
import kernel360.ckt.gateway.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RequiredArgsConstructor
@Order(0)
@Slf4j
public class JwtAuthenticationFilter implements GlobalFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Value("#{'${jwt.whitelist}'.split(',')}")
    private List<String> whitelistPaths;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        log.info("[JwtAuthFilter] 요청 경로: {}", path);

        // 1) whitelist 경로는 인증 스킵
        if (whitelistPaths.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }

        // 2) Authorization: Bearer 헤더 파싱
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange, 401, "Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        // 3) 토큰 검증
        if (!jwtTokenProvider.validationToken(token)) {
            log.warn("[JwtAuthFilter] 유효하지 않거나 만료된 토큰");
            return unauthorized(exchange, 401, "Invalid or expired token");
        }

        // 4) 유효하면 X-User-Id 헤더 추가 후 체인 이어가기
        String userId = jwtTokenProvider.getUserId(token);
        log.info("[JwtAuthFilter] 인증 성공 - 사용자 ID: {}", userId);
        ServerWebExchange mutated = exchange.mutate()
            .request(exchange.getRequest().mutate()
                .header("X-User-Id", userId)
                .build())
            .build();

        return chain.filter(mutated);
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, int status, String message) {
        try {
            ErrorResponse err = ErrorResponse.from(status, message);
            String json = objectMapper.writeValueAsString(err);
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);

            exchange.getResponse().setStatusCode(HttpStatus.valueOf(status));
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (Exception e) {
            log.error("[JwtFilter] Error serializing ErrorResponse", e);
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return exchange.getResponse().setComplete();
        }
    }
}
