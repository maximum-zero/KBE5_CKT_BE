package kernel360.ckt.collector.ui.dto.request;

import kernel360.ckt.collector.application.command.VehicleCollectorOffCommand;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record VehicleCollectorOffRequest(
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
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmss");

    public VehicleCollectorOffCommand toCommand() {
        final LocalDateTime parsedOnTime = LocalDateTime.parse(onTime, DATE_FORMATTER);
        final LocalDateTime parsedOffTime = LocalDateTime.parse(offTime, DATE_FORMATTER);
        final double parsedLat = Double.parseDouble(lat) / 1000000;
        final double parsedLon = Double.parseDouble(lon) / 1000000;
        final long parsedAng = Long.parseLong(ang);
        final long parsedSpd = Long.parseLong(spd);
        final long parsedSum = Long.parseLong(sum);

        return VehicleCollectorOffCommand.create(Long.parseLong(mdn), gcd, parsedLat, parsedLon, parsedAng, parsedSpd, parsedSum, parsedOnTime, parsedOffTime);
    }
}
