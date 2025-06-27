package kernel360.ckt.admin.application.service.command;

import kernel360.ckt.core.domain.enums.DrivingType;

import java.time.LocalDateTime;

public record DrivingLogListCommand(
    String vehicleNumber,
    String userName,
    LocalDateTime startDate,
    LocalDateTime endDate,
    DrivingType type
) {
    public static DrivingLogListCommand create(
        String vehicleNumber,
        String userName,
        LocalDateTime startDate,
        LocalDateTime endDate,
        DrivingType type
    ) {
        return new DrivingLogListCommand(vehicleNumber, userName, startDate, endDate, type);
    }
}
