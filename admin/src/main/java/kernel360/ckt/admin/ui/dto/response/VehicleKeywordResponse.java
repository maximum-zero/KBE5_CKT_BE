package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.VehicleEntity;

import java.util.List;

public record VehicleKeywordResponse(
    List<VehicleKeywordSummaryResponse> list
) {
    public static VehicleKeywordResponse from(List<VehicleEntity> vehicles) {
        return new VehicleKeywordResponse(
            vehicles.stream().map(VehicleKeywordSummaryResponse::from).toList()
        );
    }
}
