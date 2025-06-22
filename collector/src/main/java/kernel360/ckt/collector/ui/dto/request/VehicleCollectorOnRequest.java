package kernel360.ckt.collector.ui.dto.request;

import kernel360.ckt.collector.application.service.command.VehicleCollectorOnCommand;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record VehicleCollectorOnRequest(
    String mdn,
    String tid,
    String mid,
    String pv,
    String did,
    String onTime,
    String offTime,
    String gcd,
    String lat,
    String lon,
    String ang,
    String spd,
    String sum
) {
    private static final double GPS_COORDINATE_DIVISOR = 1000000.0;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmss");

    public VehicleCollectorOnCommand toCommand() {
        final long parsedMdn = Long.parseLong(mdn);
        final double parsedLat = Double.parseDouble(lat) / GPS_COORDINATE_DIVISOR;
        final double parsedLon = Double.parseDouble(lon) / GPS_COORDINATE_DIVISOR;
        final long parsedAng = Long.parseLong(ang);
        final long parsedSpd = Long.parseLong(spd);
        final long parsedSum = Long.parseLong(sum);
        final LocalDateTime parsedOnTime = LocalDateTime.parse(onTime, DATE_FORMATTER);

        return VehicleCollectorOnCommand.create(parsedMdn, gcd, parsedLat, parsedLon, parsedAng, parsedSpd, parsedSum, parsedOnTime);
    }
}
