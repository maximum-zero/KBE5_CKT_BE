package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RouteEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public record DrivingLogListResponse(
    List<DrivingLogResponse> list,
    int page,
    int size,
    long totalElements,
    int totalPages
) {
    public static DrivingLogListResponse from(Page<DrivingLogEntity> pageData, Map<Long, List<RouteEntity>> routeMap) {
        List<DrivingLogResponse> drivingLogResponseList = pageData.getContent().stream()
            .map(drivingLog -> {
                List<RouteEntity> routes = routeMap.get(drivingLog.getId());
                return DrivingLogResponse.of(drivingLog, routes);
            })
            .toList();

        return new DrivingLogListResponse(
            drivingLogResponseList,
            pageData.getNumber(),
            pageData.getSize(),
            pageData.getTotalElements(),
            pageData.getTotalPages()
        );
    }
}
