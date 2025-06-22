package kernel360.ckt.collector.infra.jpa;

import kernel360.ckt.core.domain.entity.VehicleTraceLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleTraceLogJpaRepository extends JpaRepository<VehicleTraceLogEntity, Long> {
}
