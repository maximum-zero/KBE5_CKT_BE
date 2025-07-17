package kernel360.ckt.collector.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import kernel360.ckt.collector.application.service.dto.GpsSummarySseDto;
import kernel360.ckt.collector.config.RabbitConfig;
import kernel360.ckt.collector.config.SseEmitterManager;
import kernel360.ckt.collector.ui.dto.request.VehicleCollectorCycleRequest;
import kernel360.ckt.core.domain.dto.CycleInformation;
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
    private final SseEmitterManager sseEmitterManager;
    private final ObjectMapper objectMapper;

    private static final double GPS_COORDINATE_DIVISOR = 1000000.0;

    // `addEmitter` static 메서드 제거

    // RabbitMQ에서 메시지를 받으면 모든 SSE 클라이언트에게 전송
    @RabbitListener(queues = RabbitConfig.VIEW_QUEUE)
    public void receiveGpsDataAndStreamToSse(String message) {
        try {
            final VehicleCollectorCycleRequest vehicleCollectorCycle = objectMapper.readValue(message, VehicleCollectorCycleRequest.class);
            if (vehicleCollectorCycle.cList().isEmpty()) {
                return;
            }

            final CycleInformation lastCycleInfo = vehicleCollectorCycle.cList().get(vehicleCollectorCycle.cList().size() - 1);
            final double lat = Double.parseDouble(lastCycleInfo.lat()) / GPS_COORDINATE_DIVISOR;
            final double lon = Double.parseDouble(lastCycleInfo.lon()) / GPS_COORDINATE_DIVISOR;
            final int speed = Integer.parseInt(lastCycleInfo.spd());

            final GpsSummarySseDto gpsSseDto = new GpsSummarySseDto(
                vehicleCollectorCycle.mdn(), lat, lon, speed, null
            );

            // 매니저를 통해 Emitters 리스트에 접근
            log.info("sse emitters: {} ", sseEmitterManager.getEmitters().size());
            for (SseEmitter emitter : sseEmitterManager.getEmitters()) {
                try {
                    emitter.send(SseEmitter.event().name("gps-update").data(objectMapper.writeValueAsString(gpsSseDto)));
                    log.info("SSE 스트리밍 전송 완료");
                } catch (IOException e) {
                    log.error("SSE GPS Update 전송 실패 > ", e);
                }
            }
        } catch (IOException e) {
            log.error("RabbitMQ 메시지 역직렬화 또는 SSE JSON 직렬화 실패 > ", e);
        }
    }

}
