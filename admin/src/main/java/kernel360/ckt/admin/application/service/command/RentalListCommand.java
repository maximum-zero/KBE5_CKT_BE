package kernel360.ckt.admin.application.service.command;

import java.time.LocalDateTime;
import kernel360.ckt.core.domain.enums.RentalStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RentalListCommand {
    private final Long companyId;
    private final RentalStatus status;
    private final String keyword;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;

    public static RentalListCommand create(
        Long companyId,
        RentalStatus status,
        String keyword,
        LocalDateTime startAt,
        LocalDateTime endAt
    ) {
        return new RentalListCommand(companyId, status, keyword, startAt, endAt);
    }
}
