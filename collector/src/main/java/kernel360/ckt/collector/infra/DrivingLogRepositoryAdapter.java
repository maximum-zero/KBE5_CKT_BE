package kernel360.ckt.collector.infra;

import java.util.Optional;
import kernel360.ckt.collector.application.port.DrivingLogRepository;
import kernel360.ckt.collector.infra.jpa.DrivingLogJpaRepository;
import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.entity.VehicleEntity;
import kernel360.ckt.core.domain.enums.DrivingLogStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class DrivingLogRepositoryAdapter implements DrivingLogRepository {
    private final DrivingLogJpaRepository drivingLogJpaRepository;

    @Override
    public DrivingLogEntity save(DrivingLogEntity drivingLog) {
        return drivingLogJpaRepository.save(drivingLog);
    }

    @Override
    public Optional<DrivingLogEntity> findFirstByRentalAndStatus(RentalEntity rental, DrivingLogStatus status) {
        return drivingLogJpaRepository.findFirstByRentalAndStatus(rental, status);
    }

    @Override
    public Optional<DrivingLogEntity> findFirstByVehicleAndStatusOrderByCreatedAtDesc(VehicleEntity vehicle, DrivingLogStatus status) {
        return drivingLogJpaRepository.findFirstByVehicleAndStatusOrderByCreatedAtDesc(vehicle, status);
    }
}
