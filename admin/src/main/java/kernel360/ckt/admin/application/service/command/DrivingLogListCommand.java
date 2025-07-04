package kernel360.ckt.admin.application.service.command;

import java.time.LocalDateTime;

public record DrivingLogListCommand(
    String vehicleNumber,
    LocalDateTime startDate,
    LocalDateTime endDate
) {
    public static DrivingLogListCommand create(
        String vehicleNumber,
        LocalDateTime startDate,
        LocalDateTime endDate
    ) {
        return new DrivingLogListCommand(vehicleNumber, startDate, endDate);
    }
}
