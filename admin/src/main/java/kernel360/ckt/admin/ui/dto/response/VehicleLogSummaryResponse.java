package kernel360.ckt.admin.ui.dto.response;

import java.time.LocalTime;

public record VehicleLogSummaryResponse(
    String registrationNumber,
    String companyName,
    Long drivingDays,
    Long totalDistance,
    Double averageDistance,
    String averageDrivingTime
) {
    public double drivingRate(long totalDays) {
        return totalDays == 0 ? 0 : (double) drivingDays / totalDays * 100;
    }
}
