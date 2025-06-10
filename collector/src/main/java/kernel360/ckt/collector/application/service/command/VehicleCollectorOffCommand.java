package kernel360.ckt.collector.application.service.command;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class VehicleCollectorOffCommand {
    private final Long vehicleId;
    private final String gcd;
    private final double lat;
    private final double lon;
    private final long direction;
    private final long speed;
    private final long totalDistance;
    private final LocalDateTime onTime;
    private final LocalDateTime offTime;

    public static VehicleCollectorOffCommand create(
        Long vehicleId,
        String gcd,
        double lat,
        double lon,
        long direction,
        long speed,
        long totalDistance,
        LocalDateTime onTime,
        LocalDateTime offTime
    ) {
        return new VehicleCollectorOffCommand(vehicleId, gcd, lat, lon, direction, speed, totalDistance, onTime, offTime);
    }

}
