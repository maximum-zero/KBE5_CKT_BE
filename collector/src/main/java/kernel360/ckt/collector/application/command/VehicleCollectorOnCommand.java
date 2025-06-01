package kernel360.ckt.collector.application.command;

import kernel360.ckt.core.domain.entity.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class VehicleCollectorOnCommand {
    private final Long vehicleId;
    private final String gcd;
    private final double lat;
    private final double lon;
    private final long direction;
    private final long speed;
    private final long totalDistance;
    private final LocalDateTime onTime;

    public static VehicleCollectorOnCommand create(
        Long vehicleId,
        String gcd,
        double lat,
        double lon,
        long direction,
        long speed,
        long totalDistance,
        LocalDateTime onTime
    ) {
        return new VehicleCollectorOnCommand(vehicleId, gcd, lat, lon, direction, speed, totalDistance, onTime);
    }

    public RentalEntity toRentalEntity(VehicleEntity vehicle) {
        return RentalEntity.create(vehicle, this.onTime);
    }

    public DrivingLogEntity toDrivingLogentity(RentalEntity rental, VehicleEntity vehicle) {
        return DrivingLogEntity.create(rental, vehicle);
    }

    public RouteEntity toRouteEntity(DrivingLogEntity drivingLog) {
        return RouteEntity.create(drivingLog, this.lat, this.lon, this.totalDistance, this.onTime);
    }
}
