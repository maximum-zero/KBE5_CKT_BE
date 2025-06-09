package kernel360.ckt.core.repository;

import kernel360.ckt.core.domain.entity.RouteEntity;
import kernel360.ckt.core.domain.enums.RouteStatus;

import java.util.Optional;

public interface RouteRepository {
    RouteEntity save(RouteEntity route);
    Optional<RouteEntity> findFirstByDrivingLogAndStatusOrderByStartAtDesc(Long drivingLogId, RouteStatus routeStatus);
}
