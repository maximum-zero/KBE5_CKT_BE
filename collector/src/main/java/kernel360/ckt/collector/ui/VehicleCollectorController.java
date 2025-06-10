package kernel360.ckt.collector.ui;

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

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/vehicles/collector")
@RestController
public class VehicleCollectorController {

    private final VehicleCollectorService vehicleCollectorService;

    @PostMapping("/on")
    CommonResponse<VehicleCollectorResponse> sendVehicleOn(@RequestBody VehicleCollectorOnRequest request) {
        final VehicleCollectorOnCommand command = request.toCommand();
        return CommonResponse.success(vehicleCollectorService.sendVehicleOn(command));
    }

    @PostMapping("/off")
    CommonResponse<VehicleCollectorResponse> sendVehicleOff(@RequestBody VehicleCollectorOffRequest request) {
        final VehicleCollectorOffCommand command = request.toCommand();
        return CommonResponse.success(vehicleCollectorService.sendVehicleOff(command));
    }

    @PostMapping("/cycle")
    CommonResponse<VehicleCollectorResponse> saveVehicleCycle(@RequestBody VehicleCollectorCycleRequest request) {
        final VehicleCollectorCycleCommand cycleCommand = request.toCommand();
        return CommonResponse.success(vehicleCollectorService.saveVehicleCycle(cycleCommand));
    }

}
