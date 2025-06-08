package kernel360.ckt.collector.application.service;

import jakarta.transaction.Transactional;
import kernel360.ckt.collector.application.repository.DrivingLogRepository;
import kernel360.ckt.collector.application.repository.RentalRepository;
import kernel360.ckt.collector.application.repository.RouteRepository;
import kernel360.ckt.collector.application.repository.VehicleTraceLogRepository;
import kernel360.ckt.collector.application.service.command.VehicleCollectorCycleCommand;
import kernel360.ckt.collector.application.service.command.VehicleCollectorOffCommand;
import kernel360.ckt.collector.application.service.command.VehicleCollectorOnCommand;
import kernel360.ckt.collector.ui.dto.response.VehicleCollectorResponse;
import kernel360.ckt.core.domain.entity.*;
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

    @Transactional
    public VehicleCollectorResponse sendVehicleOn(VehicleCollectorOnCommand command) {
//        final VehicleEntity vehicle = vehicleService.findById(command.getVehicleId());
//
//        final RentalEntity rental = rentalRepository.save(command.toRentalEntity(vehicle));
//
//        final DrivingLogEntity drivingLog = command.toDrivingLogentity(rental, vehicle);
//        drivingLog.inProgress();
//
//        final DrivingLogEntity savedDrivingLog = drivingLogRepository.save(drivingLog);
//        routeRepository.save(command.toRouteEntity(savedDrivingLog));

        return VehicleCollectorResponse.from(command.getVehicleId());
    }

    @Transactional
    public VehicleCollectorResponse sendVehicleOff(VehicleCollectorOffCommand command) {
        final VehicleEntity vehicle = vehicleService.findById(command.getVehicleId());

//        final RentalEntity rental = rentalRepository.findFirstByVehicleIdAndStatusAndPickupAt(vehicle.getId(), RentalStatus.RENTED, command.getOnTime())
//            .orElseThrow(() -> new RuntimeException("렌탈 정보를 찾을 수 없습니다."));
//        rental.returned(command.getOnTime());
//        rentalRepository.save(rental);

//        final DrivingLogEntity drivingLog = drivingLogRepository.findFirstByRentalIdAndStatus(rental.getId(), DrivingLogStatus.IN_PROGRESS)
//            .orElseThrow(() -> new RuntimeException("운행 일지 정보를 찾을 수 없습니다."));
//
//        drivingLog.completed();
//        drivingLogRepository.save(drivingLog);
//
//        final RouteEntity route = routeRepository.findFirstByDrivingLogIdAndStatusOrderByStartAtDesc(drivingLog.getId(), RouteStatus.ACTIVE)
//            .orElseThrow(() -> new RuntimeException("경로 정보를 찾을 수 없습니다."));
//        route.completed(command.getLat(), command.getLon(), command.getTotalDistance(), command.getOffTime());
//        routeRepository.save(route);

        return VehicleCollectorResponse.from(command.getVehicleId());
    }

    @Transactional
    public VehicleCollectorResponse saveVehicleCycle(VehicleCollectorCycleCommand command) {
        final VehicleEntity vehicle = vehicleService.findById(command.getVehicleId());

        final DrivingLogEntity drivingLog = drivingLogRepository.findFirstByVehicleIdAndStatusOrderByCreateAtDesc(vehicle.getId(), DrivingLogStatus.IN_PROGRESS)
            .orElseThrow(() -> new RuntimeException("운행 일지 정보를 찾을 수 없습니다."));

        drivingLogRepository.save(drivingLog);

        final RouteEntity route = routeRepository.findFirstByDrivingLogIdAndStatusOrderByStartAtDesc(drivingLog.getId(), RouteStatus.ACTIVE)
            .orElseThrow(() -> new RuntimeException("경로 정보를 찾을 수 없습니다."));

        VehicleTraceLogEntity vehicleTraceLog = VehicleTraceLogEntity.create(route, command.getCList(), command.getOnTime());
        vehicleTraceLogRepository.save(vehicleTraceLog);

        return VehicleCollectorResponse.from(command.getVehicleId());
    }

}
