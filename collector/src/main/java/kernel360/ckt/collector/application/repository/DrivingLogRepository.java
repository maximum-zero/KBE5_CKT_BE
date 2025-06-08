package kernel360.ckt.collector.application.repository;

import java.util.Optional;
import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.enums.DrivingLogStatus;

public interface DrivingLogRepository {
    DrivingLogEntity save(DrivingLogEntity drivingLog);

    Optional<DrivingLogEntity> findFirstByVehicleIdAndStatusOrderByCreateAtDesc(Long vehicleId, DrivingLogStatus status);
    Optional<DrivingLogEntity> findFirstByRentalIdAndStatus(Long rentalId, DrivingLogStatus status);
}
