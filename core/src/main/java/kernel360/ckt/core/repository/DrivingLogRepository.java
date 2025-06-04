package kernel360.ckt.core.repository;

import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.enums.DrivingLogStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DrivingLogRepository {
    DrivingLogEntity save(DrivingLogEntity drivingLog);
    Page<DrivingLogEntity> findAllByOrderByCreateAtDesc(Pageable pageable);

    Optional<DrivingLogEntity> findFirstByVehicleIdAndStatusOrderByCreateAtDesc(Long vehicleId, DrivingLogStatus status);
    Optional<DrivingLogEntity> findFirstByRentalIdAndStatus(Long rentalId, DrivingLogStatus status);
}
