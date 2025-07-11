package kernel360.ckt.collector.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import kernel360.ckt.collector.config.RabbitConfig;
import kernel360.ckt.collector.config.SseEmitterManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Component
@Profile("consumer")
@RequiredArgsConstructor
public class GpsViewConsumer {

    private final ObjectMapper objectMapper;
    private final SseEmitterManager sseEmitterManager;

    // `addEmitter` static 메서드 제거

    // RabbitMQ에서 메시지를 받으면 모든 SSE 클라이언트에게 전송
    @RabbitListener(queues = RabbitConfig.VIEW_QUEUE)
    public void receiveGpsDataAndStreamToSse(String message) {
        log.info("📡 관제용 MQ 메시지 수신: {}", message);

        // 매니저를 통해 Emitters 리스트에 접근
        log.info("sse emitters: {} ", sseEmitterManager.getEmitters().size());
        for (SseEmitter emitter : sseEmitterManager.getEmitters()) {
            try {
                // SseEmitter.event()를 사용하여 이벤트 형식으로 데이터 전송
                emitter.send(SseEmitter.event().name("gps-update").data(message));
                log.info("📤 SSE 스트리밍 전송 완료");
            } catch (IOException e) {
                log.error("SSE 클라이언트 전송 실패, 연결 끊김. Emitter 제거 예정.", e);
            }
        }
    }


}
