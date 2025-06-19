package kernel360.ckt.admin.ui.dto.request;

import java.time.LocalDateTime;
import kernel360.ckt.admin.application.service.command.RentalListCommand;
import kernel360.ckt.core.domain.enums.RentalStatus;
import org.springframework.format.annotation.DateTimeFormat;

public record RentalListRequest(
    RentalStatus status,
    String keyword,

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime startAt,

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
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
