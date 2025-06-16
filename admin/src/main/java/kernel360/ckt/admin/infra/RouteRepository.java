package kernel360.ckt.admin.infra;

import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RouteEntity;

import java.util.List;

public interface RouteRepository {
    RouteEntity save(RouteEntity route);
    List<RouteEntity> findByDrivingLogIn(List<DrivingLogEntity> drivingLogs);
    List<RouteEntity> findByDrivingLogId(Long drivingLogId);
}
