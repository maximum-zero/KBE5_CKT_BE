package kernel360.ckt.admin.ui.dto.request;

import kernel360.ckt.admin.application.service.command.DrivingLogListCommand;
import kernel360.ckt.core.domain.enums.DrivingType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record DrivingLogListRequest(
    String vehicleNumber,

    String userName,

    @DateTimeFormat(pattern = "yyyyMMddHHmmss")
    LocalDateTime startDate,

    @DateTimeFormat(pattern = "yyyyMMddHHmmss")
    LocalDateTime endDate,

    DrivingType type
) {
    public DrivingLogListCommand toCommand() {
        return DrivingLogListCommand.create(
            vehicleNumber, userName, startDate, endDate, type
        );
    }
}
