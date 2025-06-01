package kernel360.ckt.collector.infra.repository.jpa;

import kernel360.ckt.core.domain.entity.VehicleTraceLogEntity;
import kernel360.ckt.core.repository.VehicleTraceLogRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleTraceLogJpaRepository extends JpaRepository<VehicleTraceLogEntity, Long>, VehicleTraceLogRepository {
}
