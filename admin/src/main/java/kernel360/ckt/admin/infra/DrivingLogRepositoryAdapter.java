package kernel360.ckt.admin.infra;

import kernel360.ckt.admin.application.port.DrivingLogRepository;
import kernel360.ckt.admin.infra.jpa.DrivingLogJpaRepository;
import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.enums.DrivingType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class DrivingLogRepositoryAdapter implements DrivingLogRepository {
    private final DrivingLogJpaRepository drivingLogJpaRepository;

    @Override
    public DrivingLogEntity save(DrivingLogEntity drivingLog) {
        return drivingLogJpaRepository.save(drivingLog);
    }

    @Override
    public Page<DrivingLogEntity> findAll(String vehicleNumber, String userName, LocalDateTime startDate, LocalDateTime endDate, DrivingType type, Pageable pageable) {
        return drivingLogJpaRepository.findAll(vehicleNumber, userName, startDate, endDate, type, pageable);
    }

    @Override
    public Optional<DrivingLogEntity> findById(Long drivingLogId) {
        return drivingLogJpaRepository.findById(drivingLogId);
    }

    @Override
    public Optional<DrivingLogEntity> findByRental(RentalEntity rental) {
        return drivingLogJpaRepository.findByRental(rental);
    }
}
