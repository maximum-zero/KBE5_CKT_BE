package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.dto.CycleInformation;
import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RouteEntity;

import java.util.List;
import java.util.Map;

public record DrivingLogDetailResponse(
    DrivingLogResponse drivingLogResponse,
    List<RouteInfoResponse> routes
) {
    public static DrivingLogDetailResponse from(DrivingLogEntity drivingLogEntity, List<RouteEntity> routeEntities, Map<Long, List<CycleInformation>> routeIdToTraceLogsMap) {
        DrivingLogResponse drivingLogResponse = DrivingLogResponse.of(drivingLogEntity, routeEntities);

        List<RouteInfoResponse> routes = routeEntities.stream()
            .map(route -> {
                List<CycleInformation> traceLogs = routeIdToTraceLogsMap.getOrDefault(route.getId(), List.of());
                return RouteInfoResponse.from(route, traceLogs);
            })
            .toList();

        return new DrivingLogDetailResponse(drivingLogResponse, routes);
    }
}
