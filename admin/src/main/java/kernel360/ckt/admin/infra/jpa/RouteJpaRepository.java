package kernel360.ckt.admin.infra.jpa;

import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteJpaRepository extends JpaRepository<RouteEntity, Long> {
    List<RouteEntity> findByDrivingLogIn(List<DrivingLogEntity> drivingLogs);
    List<RouteEntity> findByDrivingLogId(Long drivingLogId);
}
