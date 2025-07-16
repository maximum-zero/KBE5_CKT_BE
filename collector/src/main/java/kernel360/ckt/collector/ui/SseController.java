package kernel360.ckt.collector.ui;

import kernel360.ckt.collector.config.SseEmitterManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequestMapping("/api/v1/sse")
@RequiredArgsConstructor
public class SseController {

    private final SseEmitterManager sseEmitterManager;

    @GetMapping(path = "/gps", produces = "text/event-stream")
    public SseEmitter streamGpsUpdates() {
        log.info("SSE 클라이언트 연결 요청 수신");
        // 연결을 영구적으로 유지하기 위해 타임아웃을 Long.MAX_VALUE로 설정
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        sseEmitterManager.add(emitter);
        log.info("SSE emitter 등록 완료");
        return emitter;
    }
}
