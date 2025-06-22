package kernel360.ckt.collector.infra.jpa;

import java.util.Optional;
import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.entity.VehicleEntity;
import kernel360.ckt.core.domain.enums.DrivingLogStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrivingLogJpaRepository extends JpaRepository<DrivingLogEntity, Long> {
    Optional<DrivingLogEntity> findFirstByRentalAndStatus(RentalEntity rental, DrivingLogStatus status);
    Optional<DrivingLogEntity> findFirstByVehicleAndStatusOrderByCreatedAtDesc(VehicleEntity vehicle, DrivingLogStatus status);
}
