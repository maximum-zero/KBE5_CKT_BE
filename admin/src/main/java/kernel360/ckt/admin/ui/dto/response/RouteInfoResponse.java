package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.dto.CycleInformation;
import kernel360.ckt.core.domain.dto.LatLng;
import kernel360.ckt.core.domain.entity.RouteEntity;

import java.time.LocalDateTime;
import java.util.List;

public record RouteInfoResponse(
    double startLat,
    double startLon,
    double endLat,
    double endLon,
    LocalDateTime startAt,
    LocalDateTime endAt,
    List<LatLng> traceLogs
) {
    public static RouteInfoResponse from(RouteEntity routeEntity, List<CycleInformation> traceLogs) {
        List<LatLng> latLngLogs = traceLogs.stream()
            .map(ci -> new LatLng(ci.lat(), ci.lon()))
            .toList();

        return new RouteInfoResponse(
            routeEntity.getStartLat(),
            routeEntity.getStartLon(),
            routeEntity.getEndLat(),
            routeEntity.getEndLon(),
            routeEntity.getStartAt(),
            routeEntity.getEndAt(),
            latLngLogs
        );
    }
}
