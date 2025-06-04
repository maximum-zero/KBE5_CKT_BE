package kernel360.ckt.core.repository;

import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RouteEntity;
import kernel360.ckt.core.domain.enums.RouteStatus;

import java.util.List;
import java.util.Optional;

public interface RouteRepository {
    RouteEntity save(RouteEntity route);
    Optional<RouteEntity> findFirstByDrivingLogIdAndStatusOrderByStartAtDesc(Long drivingLogId, RouteStatus routeStatus);
    List<RouteEntity> findByDrivingLogIn(List<DrivingLogEntity> drivingLogs);
}
