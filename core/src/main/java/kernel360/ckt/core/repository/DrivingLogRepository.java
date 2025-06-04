package kernel360.ckt.core.repository;

import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.enums.DrivingLogStatus;

import java.util.List;
import java.util.Optional;

public interface DrivingLogRepository {
    DrivingLogEntity save(DrivingLogEntity drivingLog);
    List<DrivingLogEntity> findAll();

    Optional<DrivingLogEntity> findFirstByVehicleIdAndStatusOrderByCreateAtDesc(Long vehicleId, DrivingLogStatus status);
    Optional<DrivingLogEntity> findFirstByRentalIdAndStatus(Long rentalId, DrivingLogStatus status);
}
