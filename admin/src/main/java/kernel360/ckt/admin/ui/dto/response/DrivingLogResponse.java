package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.CustomerEntity;
import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.entity.RouteEntity;
import kernel360.ckt.core.domain.enums.DrivingType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
    DrivingType drivingType,
    String statusName,
    String memo
) {
    public static DrivingLogResponse of(DrivingLogEntity drivingLogEntity, List<RouteEntity> routes) {
        Long startOdometer = routes.stream()
            .map(RouteEntity::getStartOdometer)
            .min(Long::compareTo)
            .orElseThrow(() -> new IllegalStateException("Route 목록이 비어있습니다."));

        Long endOdometer = routes.stream()
            .map(RouteEntity::getEndOdometer)
            .max(Long::compareTo)
            .orElseThrow(() -> new IllegalStateException("Route 목록이 비어있습니다."));

        String customerName = Optional.ofNullable(drivingLogEntity.getRental())
            .map(RentalEntity::getCustomer)
            .map(CustomerEntity::getCustomerName)
            .orElse(null);

        LocalDateTime startAt = routes.stream()
            .map(RouteEntity::getStartAt)
            .filter(Objects::nonNull)
            .min(LocalDateTime::compareTo)
            .orElse(null);

        LocalDateTime endAt = routes.stream()
            .map(RouteEntity::getEndAt)
            .filter(Objects::nonNull)
            .max(LocalDateTime::compareTo)
            .orElse(null);

        return new DrivingLogResponse(
            drivingLogEntity.getId(),
            drivingLogEntity.getVehicle().getModelName(),
            drivingLogEntity.getVehicle().getRegistrationNumber(),
            startAt,
            endAt,
            startOdometer,
            endOdometer,
            endOdometer - startOdometer,
            customerName,
            drivingLogEntity.getType(),
            drivingLogEntity.getType().getValue(),
            drivingLogEntity.getMemo()
        );
    }
}
