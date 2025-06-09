package kernel360.ckt.collector.application.repository;

import java.util.Optional;
import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.entity.VehicleEntity;
import kernel360.ckt.core.domain.enums.DrivingLogStatus;

public interface DrivingLogRepository {
    DrivingLogEntity save(DrivingLogEntity drivingLog);
    Optional<DrivingLogEntity> findFirstByRentalAndStatus(RentalEntity rental, DrivingLogStatus status);
    Optional<DrivingLogEntity> findFirstByVehicleAndStatusOrderByCreatedAtDesc(VehicleEntity vehicle, DrivingLogStatus status);

}
