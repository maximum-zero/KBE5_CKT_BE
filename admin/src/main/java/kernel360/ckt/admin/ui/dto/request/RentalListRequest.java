package kernel360.ckt.admin.ui.dto.request;

import java.time.LocalDateTime;
import kernel360.ckt.admin.application.service.command.RentalListCommand;
import kernel360.ckt.core.domain.enums.RentalStatus;
import org.springframework.format.annotation.DateTimeFormat;

public record RentalListRequest(
    RentalStatus status,
    String keyword,

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime startAt,

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime endAt
) {
    public RentalListCommand toCommand(Long companyId) {
        return RentalListCommand.create(
            companyId,
            this.status,
            this.keyword,
            this.startAt,
            this.endAt
        );
    }
}
