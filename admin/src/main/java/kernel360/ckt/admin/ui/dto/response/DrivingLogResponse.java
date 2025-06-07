package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.CustomerEntity;
import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.entity.RouteEntity;
import kernel360.ckt.core.domain.enums.DrivingLogStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public record DrivingLogResponse(
    Long id,
    String VehicleModelName,
    String VehicleRegistrationNumber,
    LocalDateTime startAt,
    LocalDateTime endAt,
    Long startOdometer,
    Long endOdometer,
    Long totalDistance,
    String customerName,
    DrivingLogStatus status,
    String memo
) {
    public static DrivingLogResponse of(DrivingLogEntity drivingLogEntity, List<RouteEntity> routes) {
        Long startOdometer = routes.stream()
            .map(RouteEntity::getStartOdometer)
            .min(Long::compareTo)
            .get();

        Long endOdometer = routes.stream()
            .map(RouteEntity::getEndOdometer)
            .max(Long::compareTo)
            .get();

        String customerName = Optional.ofNullable(drivingLogEntity.getRental())
            .map(RentalEntity::getCustomer)
            .map(CustomerEntity::getCustomerName)
            .orElse(null);

        return new DrivingLogResponse(
            drivingLogEntity.getId(),
            drivingLogEntity.getVehicle().getModelName(),
            drivingLogEntity.getVehicle().getRegistrationNumber(),
            drivingLogEntity.getRental().getPickupAt(),
            drivingLogEntity.getRental().getReturnAt(),
            startOdometer,
            endOdometer,
            endOdometer-startOdometer,
            customerName,
            drivingLogEntity.getStatus(),
            drivingLogEntity.getMemo()
        );
    }
}
