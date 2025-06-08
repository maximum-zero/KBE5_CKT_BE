package kernel360.ckt.collector.infra.repository.jpa;

import kernel360.ckt.collector.application.repository.DrivingLogRepository;
import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrivingLogJpaRepository extends JpaRepository<DrivingLogEntity, Long>, DrivingLogRepository {
}
