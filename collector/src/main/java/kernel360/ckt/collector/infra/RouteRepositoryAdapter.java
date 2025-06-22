package kernel360.ckt.collector.infra;

import java.time.LocalDateTime;
import java.util.Optional;
import kernel360.ckt.collector.application.port.RentalRepository;
import kernel360.ckt.collector.application.port.RouteRepository;
import kernel360.ckt.collector.infra.jpa.RentalJpaRepository;
import kernel360.ckt.collector.infra.jpa.RouteJpaRepository;
import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.entity.RouteEntity;
import kernel360.ckt.core.domain.enums.RentalStatus;
import kernel360.ckt.core.domain.enums.RouteStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class RouteRepositoryAdapter implements RouteRepository {
    private final RouteJpaRepository routeJpaRepository;

    @Override
    public RouteEntity save(RouteEntity route) {
        return routeJpaRepository.save(route);
    }

    @Override
    public Optional<RouteEntity> findFirstByDrivingLogAndStatusOrderByStartAtDesc(DrivingLogEntity drivingLog, RouteStatus routeStatus) {
        return routeJpaRepository.findFirstByDrivingLogAndStatusOrderByStartAtDesc(drivingLog, routeStatus);
    }
}
