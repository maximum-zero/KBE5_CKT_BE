package kernel360.ckt.admin.infra.repository.jpa;

import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.repository.DrivingLogRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrivingLogJpaRepository extends JpaRepository<DrivingLogEntity, Long>, DrivingLogRepository {
}
