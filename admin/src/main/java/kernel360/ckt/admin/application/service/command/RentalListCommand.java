package kernel360.ckt.admin.application.service.command;

import java.time.LocalDateTime;
import kernel360.ckt.core.domain.enums.RentalStatus;

public record RentalListCommand(
    Long companyId,
    RentalStatus status,
    String keyword,
    LocalDateTime startAt,
    LocalDateTime endAt
) {
    public RentalListCommand {
        if (startAt != null && endAt != null && startAt.isAfter(endAt)) {
            throw new IllegalArgumentException("검색 시작 시간은 종료 시간보다 이후일 수 없습니다.");
        }
    }

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
