package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.RouteEntity;

import java.time.LocalDateTime;

public record RouteInfoResponse(
    double startLat,
    double startLon,
    double endLat,
    double endLon,
    LocalDateTime startAt,
    LocalDateTime endAt
) {
    public static RouteInfoResponse from(RouteEntity routeEntity) {
        return new RouteInfoResponse(
            routeEntity.getStartLat(),
            routeEntity.getStartLon(),
            routeEntity.getEndLat(),
            routeEntity.getEndLon(),
            routeEntity.getStartAt(),
            routeEntity.getEndAt()
        );
    }
}
