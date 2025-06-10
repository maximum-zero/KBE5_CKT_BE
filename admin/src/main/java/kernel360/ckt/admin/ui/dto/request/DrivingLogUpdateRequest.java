package kernel360.ckt.admin.ui.dto.request;

import kernel360.ckt.core.domain.enums.DrivingType;

public record DrivingLogUpdateRequest(
    DrivingType type,
    String memo
) {
}
