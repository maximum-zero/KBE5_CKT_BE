package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.enums.DrivingType;

public record DrivingLogUpdateResponse(
    Long id,
    DrivingType type,
    String memo
) {
    public static DrivingLogUpdateResponse from(DrivingLogEntity drivingLogEntity) {
        return new DrivingLogUpdateResponse(
            drivingLogEntity.getId(),
            drivingLogEntity.getType(),
            drivingLogEntity.getMemo()
        );
    }
}
