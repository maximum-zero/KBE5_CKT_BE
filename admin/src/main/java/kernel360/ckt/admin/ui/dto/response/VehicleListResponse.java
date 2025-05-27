package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.VehicleEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public record VehicleListResponse(
    List<VehicleResponse> list,
    int page,
    int size,
    long totalElements,
    int totalPages
) {
    public static VehicleListResponse from(Page<VehicleEntity> pageData) {
        List<VehicleResponse> vehicleList = pageData.getContent()
            .stream()
            .map(VehicleResponse::from)
            .toList();

        return new VehicleListResponse(
            vehicleList,
            pageData.getNumber(),
            pageData.getSize(),
            pageData.getTotalElements(),
            pageData.getTotalPages()
        );
    }
}
