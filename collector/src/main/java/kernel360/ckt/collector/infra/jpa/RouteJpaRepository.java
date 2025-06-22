package kernel360.ckt.collector.infra.jpa;

import java.util.Optional;
import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RouteEntity;
import kernel360.ckt.core.domain.enums.RouteStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteJpaRepository extends JpaRepository<RouteEntity, Long> {
    Optional<RouteEntity> findFirstByDrivingLogAndStatusOrderByStartAtDesc(DrivingLogEntity drivingLog, RouteStatus routeStatus);
}
