package kernel360.ckt.admin.ui.dto.request;

import kernel360.ckt.core.domain.enums.VehicleStatus;

public record VehicleUpdateRequest(
    VehicleStatus status,
    String memo
) {
}
