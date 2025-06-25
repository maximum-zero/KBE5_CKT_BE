package kernel360.ckt.collector.application.service.command;

import kernel360.ckt.core.domain.dto.GCD;

import java.time.LocalDateTime;

public record VehicleCollectorOffCommand(
    Long vehicleId,
    GCD gcd,
    double lat,
    double lon,
    int ang,
    int spd,
    long sum,
    LocalDateTime onTime,
    LocalDateTime offTime
) {
    public static VehicleCollectorOffCommand create(
        Long vehicleId,
        GCD gcd,
        double lat,
        double lon,
        int ang,
        int spd,
        long sum,
        LocalDateTime onTime,
        LocalDateTime offTime
    ) {
        return new VehicleCollectorOffCommand(vehicleId, gcd, lat, lon, ang, spd, sum, onTime, offTime);
    }

}
