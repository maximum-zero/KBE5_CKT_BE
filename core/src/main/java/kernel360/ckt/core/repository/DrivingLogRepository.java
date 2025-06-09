package kernel360.ckt.core.repository;

import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.enums.DrivingLogStatus;

import java.util.Optional;

public interface DrivingLogRepository {
    DrivingLogEntity save(DrivingLogEntity drivingLog);

    Optional<DrivingLogEntity> findByRental(RentalEntity rental);

    Optional<DrivingLogEntity> findFirstByVehicleIdAndStatusOrderByCreatedAtDesc(Long vehicleId, DrivingLogStatus status);
    Optional<DrivingLogEntity> findFirstByRentalIdAndStatus(Long rentalId, DrivingLogStatus status);
}
