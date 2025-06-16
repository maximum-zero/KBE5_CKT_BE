package kernel360.ckt.admin.infra;

import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.enums.DrivingLogStatus;
import kernel360.ckt.core.domain.enums.DrivingType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface DrivingLogRepository {
    DrivingLogEntity save(DrivingLogEntity drivingLog);
    Page<DrivingLogEntity> searchDrivingLogs(String vehicleNumber, String userName, LocalDateTime startDate, LocalDateTime endDate, DrivingType type, Pageable pageable);
    Optional<DrivingLogEntity> findById(Long drivingLogId);

    Optional<DrivingLogEntity> findByRental(RentalEntity rental);

    Optional<DrivingLogEntity> findFirstByVehicleIdAndStatusOrderByCreatedAtDesc(Long vehicleId, DrivingLogStatus status);
    Optional<DrivingLogEntity> findFirstByRentalIdAndStatus(Long rentalId, DrivingLogStatus status);
}
