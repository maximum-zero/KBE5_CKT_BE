package kernel360.ckt.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CachingRequestBodyGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 원본 Request 를 Decorator 로 감싸서, 바디를 한 번 읽고 DataBuffer 에 캐싱
        ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public Flux<DataBuffer> getBody() {
                Flux<DataBuffer> originalBody = super.getBody();
                return DataBufferUtils.join(originalBody)
                    .flatMapMany(buffer -> {
                        byte[] content = new byte[buffer.readableByteCount()];
                        buffer.read(content);
                        DataBufferUtils.release(buffer);
                        // 캐싱된 바디를 attribute 에 저장 (필요 시 downstream 에서 꺼내 사용)
                        exchange.getAttributes().put("cachedRequestBody", content);
                        // 재생 가능한 DataBuffer 반환
                        DataBuffer wrapped = exchange.getResponse()
                            .bufferFactory()
                            .wrap(content);
                        return Flux.just(wrapped);
                    });
            }
        };

        // Decorating 한 Request 를 이용해 체인 호출
        return chain.filter(exchange.mutate()
            .request(decorator)
            .build());
    }

    @Override
    public int getOrder() {
        // 순서 조정: 필요한 경우 낮은 혹은 높은 우선순위 설정
        return Ordered.LOWEST_PRECEDENCE;
    }
}
