package kernel360.ckt.collector.infra.jpa;

import kernel360.ckt.core.domain.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleJpaRepository extends JpaRepository<VehicleEntity, Long> {
}
