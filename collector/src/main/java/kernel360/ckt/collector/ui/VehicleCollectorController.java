package kernel360.ckt.collector.ui;

import jakarta.validation.Valid;
import kernel360.ckt.collector.application.service.VehicleCollectorService;
import kernel360.ckt.collector.application.service.command.VehicleCollectorCycleCommand;
import kernel360.ckt.collector.application.service.command.VehicleCollectorOffCommand;
import kernel360.ckt.collector.application.service.command.VehicleCollectorOnCommand;
import kernel360.ckt.collector.ui.dto.request.VehicleCollectorCycleRequest;
import kernel360.ckt.collector.ui.dto.request.VehicleCollectorOffRequest;
import kernel360.ckt.collector.ui.dto.request.VehicleCollectorOnRequest;
import kernel360.ckt.collector.ui.dto.response.VehicleCollectorResponse;
import kernel360.ckt.core.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 차량 주행 API Controller
 *
 * 이 클래스는 클라이언트의 HTTP 요청을 받아 예약 관련 비즈니스 로직을 처리하는 진입점 역할을 합니다.
 * 차량의 시동 ON/OFF, 주기적인 위치 정보 전송 등의 요청을 처리합니다.
 *
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/vehicles/collector")
@RestController
public class VehicleCollectorController {

    private final VehicleCollectorService vehicleCollectorService;

    /**
     * 차량 시동 ON 정보 전송 API
     *
     * @param request 차량 시동 ON 요청 정보 {@link VehicleCollectorOnRequest} DTO
     * @return 차량 시동 ON 처리 결과 {@link VehicleCollectorResponse}
     */
    @PostMapping("/on")
    CommonResponse<VehicleCollectorResponse> sendVehicleOn(@Valid @RequestBody VehicleCollectorOnRequest request) {
        log.info("차량 시동 ON 요청 - {}", request);
        final VehicleCollectorOnCommand command = request.toCommand();
        return CommonResponse.success(vehicleCollectorService.sendVehicleOn(command));
    }

    /**
     * 차량 시동 OFF 정보 전송 API
     *
     * @param request 차량 시동 OFF 요청 정보 {@link VehicleCollectorOffRequest} DTO
     * @return 차량 시동 OFF 처리 결과 {@link VehicleCollectorResponse}
     */
    @PostMapping("/off")
    CommonResponse<VehicleCollectorResponse> sendVehicleOff(@Valid @RequestBody VehicleCollectorOffRequest request) {
        log.info("차량 시동 OFF 요청 - {}", request);
        final VehicleCollectorOffCommand command = request.toCommand();
        return CommonResponse.success(vehicleCollectorService.sendVehicleOff(command));
    }

    /**
     * 차량 주기적 위치 정보 저장 API
     *
     * @param request 차량 주기적 위치 정보 요청 정보 {@link VehicleCollectorCycleRequest} DTO
     * @return 차량 주기적 위치 정보 저장 결과 {@link VehicleCollectorResponse}
     */
    @PostMapping("/cycle")
    CommonResponse<VehicleCollectorResponse> saveVehicleCycle(@Valid @RequestBody VehicleCollectorCycleRequest request) {
        log.info("차량 주기적 위치 정보 저장 요청 - {}", request);
        final VehicleCollectorCycleCommand cycleCommand = request.toCommand();
        return CommonResponse.success(vehicleCollectorService.saveVehicleCycle(cycleCommand));
    }

}
