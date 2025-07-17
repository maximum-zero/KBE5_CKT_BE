package kernel360.ckt.admin.application.service;

import kernel360.ckt.admin.application.port.VehicleRepository;
import kernel360.ckt.admin.domain.projection.RunningVehicleProjection;
import kernel360.ckt.admin.infra.basic.TraceLogQueryRepository;
import kernel360.ckt.admin.ui.dto.response.ControlTowerSummaryResponse;
import kernel360.ckt.admin.ui.dto.response.RunningVehicleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VehicleControlTowerService {

    private final VehicleRepository vehicleRepository;
    private final TraceLogQueryRepository traceLogQueryRepository;

    public ControlTowerSummaryResponse getControlTowerSummary() {
        long total = vehicleRepository.count();

        List<RunningVehicleProjection> runningProjections = traceLogQueryRepository.findRunningVehicleLocations();
        List<Long> runningVehicleIds = runningProjections.stream()
            .map(RunningVehicleProjection::getVehicleId)
            .toList();

        long running = runningVehicleIds.toArray().length;

        long stolen = vehicleRepository.countStolenVehicles(runningVehicleIds);

        long stopped = total - running;

        return ControlTowerSummaryResponse.of((int) total, (int) running, (int) stolen, (int) stopped);
    }

    public List<RunningVehicleResponse> getRunningVehiclesFromNativeQuery() {
        List<RunningVehicleProjection> runningProjections = traceLogQueryRepository.findRunningVehicleLocations();
        List<Long> runningVehicleIds = runningProjections.stream()
            .map(RunningVehicleProjection::getVehicleId)
            .toList();

        // 도난 차량 ID 목록 조회
        List<Long> stolenVehicleIds = vehicleRepository.findStolenVehicleIds(runningVehicleIds);

        // 각 차량에 대해 도난 여부를 반영하여 DTO 생성
        return runningProjections.stream()
            .map(p -> RunningVehicleResponse.from(p, stolenVehicleIds.contains(p.getVehicleId())))
            .toList();
    }
}
