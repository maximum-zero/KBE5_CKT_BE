package kernel360.ckt.collector.infra.repository.jpa;

import kernel360.ckt.collector.application.repository.VehicleTraceLogRepository;
import kernel360.ckt.core.domain.entity.VehicleTraceLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleTraceLogJpaRepository extends JpaRepository<VehicleTraceLogEntity, Long>,
    VehicleTraceLogRepository {
}
