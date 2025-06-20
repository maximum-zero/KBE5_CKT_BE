package kernel360.ckt.admin.ui.dto.request;

import jakarta.validation.constraints.Size;
import kernel360.ckt.core.domain.enums.DrivingType;

public record DrivingLogUpdateRequest(
    DrivingType type,
    @Size(max = 500, message = "메모는 500자 이하이어야 합니다.") String memo
) {
}
