package kernel360.ckt.admin.infra.jpa;

import kernel360.ckt.core.domain.entity.VehicleTraceLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleTraceLogJpaRepository extends JpaRepository<VehicleTraceLogEntity, Long> {
    List<VehicleTraceLogEntity> findByRouteId(Long routeId);
}
