package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.VehicleEntity;

import java.util.List;

public record VehicleKeywordListResponse(
    List<VehicleKeywordResponse> list
) {
        public static VehicleKeywordListResponse from(List<VehicleEntity> vehicles) {
            return new VehicleKeywordListResponse(vehicles
                .stream()
                .map(VehicleKeywordResponse::from)
                .toList()
            );
        }
    }
