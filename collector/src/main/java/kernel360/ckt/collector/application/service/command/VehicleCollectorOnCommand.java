package kernel360.ckt.collector.application.service.command;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import kernel360.ckt.core.domain.dto.GCD;
import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.entity.RouteEntity;
import kernel360.ckt.core.domain.entity.VehicleEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;

public record VehicleCollectorOnCommand(
    Long vehicleId,
    GCD gcd,
    double lat,
    double lon,
    int ang,
    int spd,
    long sum,
    LocalDateTime onTime
) {
    public static VehicleCollectorOnCommand create(
        Long vehicleId,
        GCD gcd,
        double lat,
        double lon,
        int ang,
        int spd,
        long sum,
        LocalDateTime onTime
    ) {
        return new VehicleCollectorOnCommand(vehicleId, gcd, lat, lon, ang, spd, sum, onTime);
    }

    public DrivingLogEntity toDrivingLogEntity(RentalEntity rental, VehicleEntity vehicle) {
        return DrivingLogEntity.create(rental, vehicle);
    }

    public RouteEntity toRouteEntity(DrivingLogEntity drivingLog) {
        return RouteEntity.create(drivingLog, this.lat, this.lon, this.sum, this.onTime);
    }
}
