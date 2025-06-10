package kernel360.ckt.core.repository;

import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RouteEntity;
import kernel360.ckt.core.domain.enums.RouteStatus;

import java.util.List;
import java.util.Optional;

public interface RouteRepository {
    RouteEntity save(RouteEntity route);
    List<RouteEntity> findByDrivingLogIn(List<DrivingLogEntity> drivingLogs);
    List<RouteEntity> findByDrivingLogId(Long drivingLogId);
    Optional<RouteEntity> findFirstByDrivingLogAndStatusOrderByStartAtDesc(Long drivingLogId, RouteStatus routeStatus);
}
