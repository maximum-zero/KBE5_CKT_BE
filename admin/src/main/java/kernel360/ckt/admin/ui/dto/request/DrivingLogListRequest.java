package kernel360.ckt.admin.ui.dto.request;

import kernel360.ckt.admin.application.service.command.DrivingLogListCommand;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record DrivingLogListRequest(
    String vehicleNumber,

    @DateTimeFormat(pattern = "yyyyMMddHHmmss")
    LocalDateTime startDate,

    @DateTimeFormat(pattern = "yyyyMMddHHmmss")
    LocalDateTime endDate
) {
    public DrivingLogListCommand toCommand() {
        return DrivingLogListCommand.create(
            vehicleNumber, startDate, endDate
        );
    }
}
