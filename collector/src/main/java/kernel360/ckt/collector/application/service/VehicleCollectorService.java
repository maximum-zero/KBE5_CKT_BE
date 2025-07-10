package kernel360.ckt.collector.application.service;

import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.Optional;

import kernel360.ckt.collector.application.port.*;
import kernel360.ckt.collector.application.service.command.VehicleCollectorCycleCommand;
import kernel360.ckt.collector.application.service.command.VehicleCollectorOffCommand;
import kernel360.ckt.collector.application.service.command.VehicleCollectorOnCommand;
import kernel360.ckt.collector.ui.dto.response.VehicleCollectorResponse;
import kernel360.ckt.core.common.error.VehicleErrorCode;
import kernel360.ckt.core.common.error.VehicleEventErrorCode;
import kernel360.ckt.core.common.exception.CustomException;
import kernel360.ckt.core.domain.dto.CycleInformation;
import kernel360.ckt.core.domain.entity.*;
import kernel360.ckt.core.domain.enums.DrivingLogStatus;
import kernel360.ckt.core.domain.enums.RentalStatus;
import kernel360.ckt.core.domain.enums.RouteStatus;
import kernel360.ckt.core.domain.enums.VehicleEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class VehicleCollectorService {

    private final VehicleRepository vehicleRepository;
    private final VehicleEventRepository vehicleEventRepository;
    private final RentalRepository rentalRepository;
    private final DrivingLogRepository drivingLogRepository;
    private final RouteRepository routeRepository;
    private final VehicleTraceLogRepository vehicleTraceLogRepository;

    private static final double GPS_COORDINATE_DIVISOR = 1000000.0;

    /**
     * 차량 운행을 시작합니다.
     * 활성 렌탈을 확인하고, 기존 운행일지가 없으면 새로 생성하며, 항상 새로운 경로를 시작합니다.
     */
    @Transactional
    public VehicleCollectorResponse sendVehicleOn(VehicleCollectorOnCommand command) {
        log.info("시동 ON - 요청값 : {}", command);

        // 차량 조회
        final VehicleEntity vehicle = findVehicle(command.mdn());

        // 차량 이벤트 조회 - 이미 ON이 되어 있으면 예외 발생
        final Optional<VehicleEventEntity> lastVehicleEvent = vehicleEventRepository.findFirstByVehicleIdOrderByCreatedAtDesc(vehicle.getId());
        if (lastVehicleEvent.isPresent() && lastVehicleEvent.get().getType().isON()) {
            throw new CustomException(VehicleEventErrorCode.ALREADY_RUNNING);
        }

        // 차량 위치 업데이트
        vehicle.updateLocation(command.lat(), command.lon());
        final VehicleEntity updatedVehicle = vehicleRepository.save(vehicle);
        log.info("차량 위치 및 총 주행거리 업데이트 - 위치 : {}, {}, 총 주행거리 : {}", updatedVehicle.getLat(), updatedVehicle.getLon(), updatedVehicle.getOdometer());

        // 예약 여부 확인
        final RentalEntity rental = rentalRepository.findActiveRental(updatedVehicle.getId(), command.onTime(), RentalStatus.RENTED)
            .orElse(null);

        // 운행 일지 시작
        DrivingLogEntity drivingLog;
        if (rental != null) {
            // 예약이 있는 경우: 해당 렌탈과 연관된 진행 중인 운행 일지 재사용 시도
            final Optional<DrivingLogEntity> existingInProgressLogForRental =
                drivingLogRepository.findFirstByRentalAndStatus(rental, DrivingLogStatus.IN_PROGRESS);

            if (existingInProgressLogForRental.isPresent()) {
                drivingLog = existingInProgressLogForRental.get();
                log.info("기존 운행일지 유지 - 운행일지 ID : {}", drivingLog.getId());
            } else {
                drivingLog = createAndSaveNewDrivingLog(command, updatedVehicle, rental);
            }
        } else {
            drivingLog = createAndSaveNewDrivingLog(command, updatedVehicle, null);
        }

        // 새로운 경로 시작
        final RouteEntity route = routeRepository.save(command.toRouteEntity(drivingLog, updatedVehicle));
        log.info("새로운 경로 생성 - 경로 ID : {}", route.getId());

        // 차량 이벤트 저장
        final Long rentalIdForEvent = Optional.ofNullable(rental).map(RentalEntity::getId).orElse(null);
        final VehicleEventEntity vehicleEvent = VehicleEventEntity.create(command.mdn(), VehicleEventType.ON, rentalIdForEvent);
        vehicleEventRepository.save(vehicleEvent);
        log.info("차량 이벤트 ON - 차량 이벤트 ID : {}", vehicleEvent.getId());

        return VehicleCollectorResponse.from(command.mdn());
    }

    /**
     * 차량 운행을 종료합니다.
     * 진행 중인 운행일지와 활성 경로를 찾아 경로를 완료 처리합니다.
     * 예약이 존재하지 않는 운행일지의 경우 즉시 완료되며, 예약이 완료된 운행일지의 경우에는 예약 완료에서 처리가 됩니다.
     */
    @Transactional
    public VehicleCollectorResponse sendVehicleOff(VehicleCollectorOffCommand command) {
        log.info("시동 OFF - 요청값 : {}", command);

        // 차량 조회
        final VehicleEntity vehicle = findVehicle(command.mdn());

        // 차량 이벤트 조회 - 이미 OFF이 되어 있으면 예외 발생
        final Optional<VehicleEventEntity> lastVehicleEvent = vehicleEventRepository.findFirstByVehicleIdOrderByCreatedAtDesc(vehicle.getId());
        if (lastVehicleEvent.isEmpty() || lastVehicleEvent.get().getType().isOFF()) {
            throw new CustomException(VehicleEventErrorCode.NOT_RUNNING);
        }

        // 진행 중인 운행일지를 찾습니다.
        final DrivingLogEntity drivingLog = findActiveDrivingLog(vehicle);
        log.info("진행 중인 운행일지 - 운행일지 ID : {}", drivingLog.getId());

        // 해당 운행일지에 연결된 활성 경로를 찾습니다.
        final RouteEntity route = findActiveRoute(drivingLog);
        log.info("진행 중인 경로 - 경로 ID : {}", route.getId());

        // 차량 위치 및 업데이트
        vehicle.updateLocation(command.lat(), command.lon());
        vehicle.updateOdometer(command.totalDistance());
        final VehicleEntity updatedVehicle = vehicleRepository.save(vehicle);
        log.info("차량 위치 및 총 주행거리 업데이트 - 위치 : {}, {}, 총 주행거리 : {}", updatedVehicle.getLat(), updatedVehicle.getLon(), updatedVehicle.getOdometer());

        // 경로를 완료 상태로 업데이트하고 저장
        route.completed(command.lat(), command.lon(), command.totalDistance(), command.offTime());
        routeRepository.save(route);
        log.info("경로 완료 - 경로 ID : {}", route.getId());

        final RentalEntity relatedRental = drivingLog.getRental();

        if (relatedRental == null) {
            // 예약이 없는 운행일지라면, 시동 OFF 시 즉시 완료 처리
            drivingLog.completed();
            drivingLogRepository.save(drivingLog);
            log.info("완료된 운행일지 - 운행일지 ID : {}", drivingLog.getId());
        }

        // 차량 이벤트 OFF 저장
        final Long rentalIdForEvent = (relatedRental != null) ? relatedRental.getId() : null;

        final VehicleEventEntity vehicleEvent = VehicleEventEntity.create(
            command.mdn(), VehicleEventType.OFF, rentalIdForEvent
        );
        vehicleEventRepository.save(vehicleEvent);
        log.info("차량 이벤트 OFF - 차량 이벤트 ID : {}", vehicleEvent.getId());

        return VehicleCollectorResponse.from(command.mdn());
    }

    /**
     * 운행 중 차량의 주기 정보를 저장합니다.
     * 진행 중인 운행일지와 활성 경로를 찾아 해당 정보를 기록합니다.
     */
    @Transactional
    public VehicleCollectorResponse saveVehicleCycle(VehicleCollectorCycleCommand command) {
        log.info("차량 주기 정보 - 요청값 : {}", command.mdn());

        final VehicleEntity vehicle = findVehicle(command.mdn());

        // 진행 중인 운행일지를 찾습니다.
        final DrivingLogEntity drivingLog = findActiveDrivingLog(vehicle);
        log.info("진행 중인 운행일지 - 운행일지 ID : {}", drivingLog.getId());

        // 해당 운행일지에 연결된 활성 경로를 찾습니다.
        final RouteEntity route = findActiveRoute(drivingLog);
        log.info("진행 중인 경로 - 경로 ID : {}", route.getId());

        // 새로운 차량 추적 로그를 생성하고 저장
        final VehicleTraceLogEntity vehicleTraceLog = VehicleTraceLogEntity.create(route, command.cList(), command.onTime());
        vehicleTraceLogRepository.save(vehicleTraceLog);
        log.info("완료된 주기정보 - 주기정보 ID : {}", vehicleTraceLog.getId());

        // 차량 위치 업데이트
        final Optional<CycleInformation> lastElementOptional = Optional.ofNullable(command.cList())
            .filter(list -> !list.isEmpty())
            .map(list -> list.get(list.size() - 1));

        if (lastElementOptional.isPresent()) {
            final CycleInformation cycleInformation = lastElementOptional.get();
            final double lat = Double.parseDouble(cycleInformation.lat()) / GPS_COORDINATE_DIVISOR;
            final double lon = Double.parseDouble(cycleInformation.lon()) / GPS_COORDINATE_DIVISOR;

            vehicle.updateLocation(lat, lon);
            vehicleRepository.save(vehicle);
        };

        return VehicleCollectorResponse.from(command.mdn());
    }

    /**
     * 차량의 ID로 차량을 조회합니다.
     *
     * @param vehicleId
     * @return
     */
    private VehicleEntity findVehicle(Long vehicleId) {
        return vehicleRepository.findById(vehicleId)
            .orElseThrow(() -> new CustomException(VehicleErrorCode.VEHICLE_NOT_FOUND, vehicleId));
    }

    /**
     * 새로운 DrivingLogEntity 를 생성하고 IN_PROGRESS 상태로 저장합니다.
     * @param command Vehicle ON 요청 커맨드
     * @param vehicle 해당 차량 엔티티
     * @param rental DrivingLog 에 연결될 Rental 엔티티 (없으면 null)
     * @return 새로 생성되어 저장된 DrivingLogEntity
     */
    private DrivingLogEntity createAndSaveNewDrivingLog(
        VehicleCollectorOnCommand command,
        VehicleEntity vehicle,
        @Nullable RentalEntity rental
    ) {
        DrivingLogEntity newDrivingLog = command.toDrivingLogEntity(rental, vehicle);
        newDrivingLog.inProgress();
        newDrivingLog = drivingLogRepository.save(newDrivingLog);
        log.info("새로운 운행일지 생성 및 저장 완료 - ID {}", newDrivingLog.getId());
        return newDrivingLog;
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
