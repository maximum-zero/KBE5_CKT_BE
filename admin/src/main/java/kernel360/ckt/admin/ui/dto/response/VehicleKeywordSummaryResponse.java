package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.VehicleEntity;

public record VehicleKeywordSummaryResponse(
    Long id,
    String modelName,
    String registrationNumber
) {
    public static VehicleKeywordSummaryResponse from(VehicleEntity vehicleEntity) {
        return new VehicleKeywordSummaryResponse(vehicleEntity.getId(), vehicleEntity.getModelName(), vehicleEntity.getRegistrationNumber());
    }
}
