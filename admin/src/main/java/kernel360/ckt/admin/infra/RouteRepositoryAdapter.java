package kernel360.ckt.admin.infra;

import kernel360.ckt.admin.application.port.RouteRepository;
import kernel360.ckt.admin.infra.jpa.RouteJpaRepository;
import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RouteEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class RouteRepositoryAdapter implements RouteRepository {
    private final RouteJpaRepository routeJpaRepository;

    @Override
    public RouteEntity save(RouteEntity route) {
        return routeJpaRepository.save(route);
    }

    @Override
    public List<RouteEntity> findByDrivingLogIn(List<DrivingLogEntity> drivingLogs) {
        return routeJpaRepository.findByDrivingLogIn(drivingLogs);
    }

    @Override
    public List<RouteEntity> findByDrivingLogId(Long drivingLogId) {
        return routeJpaRepository.findByDrivingLogId(drivingLogId);
    }
}
