package kernel360.ckt.collector.application.service.command;

import kernel360.ckt.core.domain.dto.GCD;

import java.time.LocalDateTime;

public record VehicleCollectorOffCommand(
    Long mdn,
    GCD gcd,
    double lat,
    double lon,
    int ang,
    int spd,
    long totalDistance,
    LocalDateTime onTime,
    LocalDateTime offTime
) {
    public static VehicleCollectorOffCommand create(
        Long mdn, GCD gcd, double lat, double lon, int ang, int spd, long totalDistance, LocalDateTime onTime, LocalDateTime offTime
    ) {
        return new VehicleCollectorOffCommand(mdn, gcd, lat, lon, ang, spd, totalDistance, onTime, offTime);
    }

}
