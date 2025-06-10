package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RouteEntity;

import java.util.List;

public record DrivingLogDetailResponse(
    DrivingLogResponse drivingLogResponse,
    List<RouteInfoResponse> routes
) {
    public static DrivingLogDetailResponse from(DrivingLogEntity drivingLogEntity, List<RouteEntity> routeEntities) {
        DrivingLogResponse drivingLogResponse = DrivingLogResponse.of(drivingLogEntity, routeEntities);
        List<RouteInfoResponse> routes = routeEntities.stream()
            .map(RouteInfoResponse::from)
            .toList();

        return new DrivingLogDetailResponse(drivingLogResponse, routes);
    }
}
