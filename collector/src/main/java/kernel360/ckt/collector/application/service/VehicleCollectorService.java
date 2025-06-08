package kernel360.ckt.collector.application.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.Optional;
import kernel360.ckt.collector.application.repository.DrivingLogRepository;
import kernel360.ckt.collector.application.repository.RentalRepository;
import kernel360.ckt.collector.application.repository.RouteRepository;
import kernel360.ckt.collector.application.repository.VehicleTraceLogRepository;
import kernel360.ckt.collector.application.service.command.VehicleCollectorCycleCommand;
import kernel360.ckt.collector.application.service.command.VehicleCollectorOffCommand;
import kernel360.ckt.collector.application.service.command.VehicleCollectorOnCommand;
import kernel360.ckt.collector.ui.dto.response.VehicleCollectorResponse;
import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.entity.RouteEntity;
import kernel360.ckt.core.domain.entity.VehicleEntity;
import kernel360.ckt.core.domain.entity.VehicleTraceLogEntity;
import kernel360.ckt.core.domain.enums.DrivingLogStatus;
import kernel360.ckt.core.domain.enums.RentalStatus;
import kernel360.ckt.core.domain.enums.RouteStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VehicleCollectorService {

    private final VehicleService vehicleService;
    private final RentalRepository rentalRepository;
    private final DrivingLogRepository drivingLogRepository;
    private final RouteRepository routeRepository;
    private final VehicleTraceLogRepository vehicleTraceLogRepository;

    /**
     * 차량 운행을 시작합니다.
     * 활성 렌탈을 확인하고, 기존 운행일지가 없으면 새로 생성하며, 항상 새로운 경로를 시작합니다.
     */
    @Transactional
    public VehicleCollectorResponse sendVehicleOn(VehicleCollectorOnCommand command) {
        final VehicleEntity vehicle = vehicleService.findById(command.getVehicleId());

        // 예약 여부를 확인합니다.
        final RentalEntity rental = rentalRepository.findActiveRental(vehicle.getId(), command.getOnTime(), RentalStatus.RENTED)
            .orElseThrow(() -> new EntityNotFoundException("예약된 차량이 아닙니다."));

        // 진행 중인 운행일지가 존재하는지 찾습니다.
        Optional<DrivingLogEntity> existingDrivingLog = drivingLogRepository.findFirstByRentalAndStatus(rental, DrivingLogStatus.IN_PROGRESS);

        final DrivingLogEntity drivingLog;
        if (existingDrivingLog.isPresent()) {
            // 기존 운행일지가 있다면 재사용 (하나의 렌탈 당 하나의 운행일지 유지)
            drivingLog = existingDrivingLog.get();
        } else {
            // 기존 운행일지가 없다면 새로 생성하여 IN_PROGRESS 상태로 저장
            final DrivingLogEntity newDrivingLog = command.toDrivingLogEntity(rental, vehicle);
            newDrivingLog.inProgress();
            drivingLog = drivingLogRepository.save(newDrivingLog);
        }

        // 새로운 경로 시작
        routeRepository.save(command.toRouteEntity(drivingLog));

        return VehicleCollectorResponse.from(command.getVehicleId());
    }

    /**
     * 차량 운행을 종료합니다.
     * 진행 중인 운행일지와 활성 경로를 찾아 경로를 완료 처리합니다. (운행일지 자체는 완료하지 않음)
     */
    @Transactional
    public VehicleCollectorResponse sendVehicleOff(VehicleCollectorOffCommand command) {
        final VehicleEntity vehicle = vehicleService.findById(command.getVehicleId());

        // 진행 중인 운행일지를 찾습니다.
        final DrivingLogEntity drivingLog = findActiveDrivingLog(vehicle);

        // 해당 운행일지에 연결된 활성 경로를 찾습니다.
        final RouteEntity route = findActiveRoute(drivingLog);

        // 경로를 완료 상태로 업데이트하고 저장
        route.completed(command.getLat(), command.getLon(), command.getTotalDistance(), command.getOffTime());
        routeRepository.save(route);

        return VehicleCollectorResponse.from(command.getVehicleId());
    }

    /**
     * 운행 중 차량의 주기 정보를 저장합니다.
     * 진행 중인 운행일지와 활성 경로를 찾아 해당 정보를 기록합니다.
     */
    @Transactional
    public VehicleCollectorResponse saveVehicleCycle(VehicleCollectorCycleCommand command) {
        final VehicleEntity vehicle = vehicleService.findById(command.getVehicleId());

        // 진행 중인 운행일지를 찾습니다.
        final DrivingLogEntity drivingLog = findActiveDrivingLog(vehicle);

        // 해당 운행일지에 연결된 활성 경로를 찾습니다.
        final RouteEntity route = findActiveRoute(drivingLog);

        // 새로운 차량 추적 로그를 생성하고 저장
        VehicleTraceLogEntity vehicleTraceLog = VehicleTraceLogEntity.create(route, command.getCList(), command.getOnTime());
        vehicleTraceLogRepository.save(vehicleTraceLog);

        return VehicleCollectorResponse.from(command.getVehicleId());
    }

    /**
     * 특정 차량에 대해 진행 중인 운행일지(DrivingLog)를 찾습니다.
     *
     * @param vehicle 운행일지를 찾을 대상 차량 엔티티
     * @return 찾은 DrivingLogEntity 객체
     * @throws EntityNotFoundException 진행 중인 운행일지를 찾을 수 없을 경우
     */
    private DrivingLogEntity findActiveDrivingLog(VehicleEntity vehicle) {
        return drivingLogRepository.findFirstByVehicleAndStatusOrderByCreatedAtDesc(vehicle, DrivingLogStatus.IN_PROGRESS)
            .orElseThrow(() -> new EntityNotFoundException("운행 일지 정보를 찾을 수 없습니다."));
    }

    /**
     * 특정 운행일지(DrivingLog)에 연결된 활성 경로(Route)를 찾습니다.
     *
     * @param drivingLog 활성 경로를 찾을 대상 운행일지 엔티티
     * @return 찾은 RouteEntity 객체
     * @throws EntityNotFoundException 활성 경로를 찾을 수 없을 경우
     */
    private RouteEntity findActiveRoute(DrivingLogEntity drivingLog) {
        return routeRepository.findFirstByDrivingLogAndStatusOrderByStartAtDesc(drivingLog, RouteStatus.ACTIVE)
            .orElseThrow(() -> new EntityNotFoundException("경로 정보를 찾을 수 없습니다."));
    }
}
