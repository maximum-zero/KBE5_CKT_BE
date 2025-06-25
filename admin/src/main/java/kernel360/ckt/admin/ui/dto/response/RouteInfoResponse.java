package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.RouteEntity;

import java.time.LocalDateTime;
import org.locationtech.jts.geom.Point;

public record RouteInfoResponse(
    Point startLocation,
    Point endLocation,
    LocalDateTime startAt,
    LocalDateTime endAt
) {
    public static RouteInfoResponse from(RouteEntity routeEntity) {
        return new RouteInfoResponse(
            routeEntity.getStartLocation(),
            routeEntity.getEndLocation(),
            routeEntity.getStartAt(),
            routeEntity.getEndAt()
        );
    }
}
