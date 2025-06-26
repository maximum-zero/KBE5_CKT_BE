package kernel360.ckt.collector.infra.jpa;

import kernel360.ckt.core.domain.entity.VehicleEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleEventJpaRepository extends JpaRepository<VehicleEventEntity, Long> {
    Optional<VehicleEventEntity> findFirstByVehicleIdOrderByCreatedAtDesc(Long vehicleId);
}
