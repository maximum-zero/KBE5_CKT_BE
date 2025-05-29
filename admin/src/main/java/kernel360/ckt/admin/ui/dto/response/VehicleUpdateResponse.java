package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.VehicleEntity;
import kernel360.ckt.core.domain.enums.VehicleStatus;

public record VehicleUpdateResponse(
    Long id,
    VehicleStatus status,
    String memo
) {
    public static VehicleUpdateResponse from(VehicleEntity vehicleEntity) {
        return new VehicleUpdateResponse(
            vehicleEntity.getId(),
            vehicleEntity.getStatus(),
            vehicleEntity.getMemo()
        );
    }
}
