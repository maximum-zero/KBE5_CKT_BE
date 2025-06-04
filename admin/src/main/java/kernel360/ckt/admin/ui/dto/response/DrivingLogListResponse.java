package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RouteEntity;

import java.util.List;
import java.util.Map;

public record DrivingLogListResponse(
    List<DrivingLogResponse> list
) {
    public static DrivingLogListResponse from(List<DrivingLogEntity> drivingLogs, Map<Long, List<RouteEntity>> routeMap) {
        List<DrivingLogResponse> drivingLogResponseList = drivingLogs.stream()
            .map(drivingLog -> {
                List<RouteEntity> routes = routeMap.get(drivingLog.getId());
                return DrivingLogResponse.of(drivingLog, routes);
            })
            .toList();

        return new DrivingLogListResponse(drivingLogResponseList);
    }
}
