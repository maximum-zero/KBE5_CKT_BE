package kernel360.ckt.collector.application.service.command;

import java.time.LocalDateTime;

import jakarta.annotation.Nullable;
import kernel360.ckt.core.domain.dto.GCD;
import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.entity.RouteEntity;
import kernel360.ckt.core.domain.entity.VehicleEntity;

public record VehicleCollectorOnCommand(
    Long mdn,
    GCD gcd,
    double lat,
    double lon,
    int ang,
    int spd,
    long totalDistance,
    LocalDateTime onTime
) {
    public static VehicleCollectorOnCommand create(
        Long mdn, GCD gcd, double lat, double lon, int ang, int spd, long totalDistance, LocalDateTime onTime
    ) {
        return new VehicleCollectorOnCommand(mdn, gcd, lat, lon, ang, spd, totalDistance, onTime);
    }

    public DrivingLogEntity toDrivingLogEntity(@Nullable RentalEntity rental, VehicleEntity vehicle) {
        return DrivingLogEntity.create(rental, vehicle);
    }

    public RouteEntity toRouteEntity(DrivingLogEntity drivingLog, VehicleEntity vehicle) {
        final Long odometer = vehicle.getOdometer();
        final long startOdometer = ((odometer != null) ? odometer : 0L) + this.totalDistance;
        return RouteEntity.create(drivingLog, this.lat, this.lon, startOdometer, this.onTime);
    }
}
