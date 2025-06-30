package kernel360.ckt.admin.application.service;

import kernel360.ckt.admin.application.port.VehicleRepository;
import kernel360.ckt.admin.domain.projection.RunningVehicleProjection;
import kernel360.ckt.admin.infra.basic.TraceLogQueryRepository;
import kernel360.ckt.admin.infra.jpa.RentalJpaRepository;
import kernel360.ckt.admin.ui.dto.response.ControlTowerSummaryResponse;
import kernel360.ckt.admin.ui.dto.response.RunningVehicleResponse;
import kernel360.ckt.core.domain.enums.RentalStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VehicleControlTowerService {

    private final VehicleRepository vehicleRepository;
    private final TraceLogQueryRepository traceLogQueryRepository;
    private final RentalJpaRepository rentalJpaRepository;

    public ControlTowerSummaryResponse getControlTowerSummary() {
        // 전체 차량 수
        long total = vehicleRepository.count();

        // 운행 중 차량 리스트
        List<RunningVehicleProjection> runningProjections = traceLogQueryRepository.findRunningVehicleLocations();
        List<Long> runningVehicleIds = runningProjections.stream()
            .map(RunningVehicleProjection::getVehicleId)
            .toList();

        long running = runningVehicleIds.isEmpty()
            ? 0
            : rentalJpaRepository.countVehiclesByVehicleIds(runningVehicleIds);

        long stopped = total - running;

        return ControlTowerSummaryResponse.of((int) total, (int) running, (int) stopped);
    }

    public List<RunningVehicleResponse> getRunningVehiclesFromNativeQuery() {
        return traceLogQueryRepository.findRunningVehicleLocations().stream()
            .map(RunningVehicleResponse::from)
            .toList();
    }
}
