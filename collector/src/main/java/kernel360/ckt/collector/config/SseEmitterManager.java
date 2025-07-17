package kernel360.ckt.collector.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
public class SseEmitterManager {

    private final CopyOnWriteArrayList<SseEmitter> sseEmitters = new CopyOnWriteArrayList<>();

    public void add(SseEmitter emitter) {
        this.sseEmitters.add(emitter);
        log.debug("새로운 SSE 클라이언트 연결됨. 현재 클라이언트 수: {}", this.sseEmitters.size());

        // 타임아웃, 완료 시 리스트에서 제거
        emitter.onCompletion(() -> {
            this.sseEmitters.remove(emitter);
            log.debug("SSE 연결 완료로 제거됨. 현재 클라이언트 수: {}", this.sseEmitters.size());
        });
        emitter.onTimeout(() -> {
            this.sseEmitters.remove(emitter);
            log.debug("SSE 연결 타임아웃으로 제거됨. 현재 클라이언트 수: {}", this.sseEmitters.size());
        });
        emitter.onError(throwable -> {
            this.sseEmitters.remove(emitter);
        });
    }

    public CopyOnWriteArrayList<SseEmitter> getEmitters() {
        return sseEmitters;
    }
}
