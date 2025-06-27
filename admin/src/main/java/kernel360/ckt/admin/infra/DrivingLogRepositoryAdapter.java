package kernel360.ckt.admin.infra;

import kernel360.ckt.admin.application.port.DrivingLogRepository;
import kernel360.ckt.admin.application.service.command.DrivingLogListCommand;
import kernel360.ckt.admin.application.service.dto.DrivingLogListDto;
import kernel360.ckt.admin.infra.jdbc.DrivingLogJdbcTemplate;
import kernel360.ckt.admin.infra.jpa.DrivingLogJpaRepository;
import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RentalEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class DrivingLogRepositoryAdapter implements DrivingLogRepository {
    private final DrivingLogJpaRepository drivingLogJpaRepository;
    private final DrivingLogJdbcTemplate drivingLogJdbcTemplate;

    @Override
    public DrivingLogEntity save(DrivingLogEntity drivingLog) {
        return drivingLogJpaRepository.save(drivingLog);
    }

    @Override
    public Page<DrivingLogListDto> findAll(DrivingLogListCommand command, Pageable pageable) {
        return drivingLogJdbcTemplate.findAll(command, pageable);
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
