package kernel360.ckt.admin.infra;

import kernel360.ckt.admin.application.port.RentalRepository;
import kernel360.ckt.admin.infra.jpa.RentalJpaRepository;
import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.entity.VehicleEntity;
import kernel360.ckt.core.domain.enums.RentalStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class RentalRepositoryAdapter implements RentalRepository {
    private final RentalJpaRepository rentalJpaRepository;

    @Override
    public RentalEntity save(RentalEntity rental) {
        return rentalJpaRepository.save(rental);
    }

    @Override
    public Page<RentalEntity> findAll(Long companyId, RentalStatus status, String keyword, LocalDateTime startAt, LocalDateTime endAt, Pageable pageable) {
        return rentalJpaRepository.findAll(companyId, status, keyword, startAt, endAt, pageable);
    }

    @Override
    public Optional<RentalEntity> findById(Long id) {
        return rentalJpaRepository.findById(id);
    }

    @Override
    public List<RentalEntity> findOverlappingRentalsByVehicleAndStatuses(VehicleEntity vehicle, List<RentalStatus> statuses, LocalDateTime pickupAt, LocalDateTime returnAt) {
        return rentalJpaRepository.findOverlappingRentalsByVehicleAndStatuses(vehicle, statuses, pickupAt, returnAt);
    }

    @Override
    public List<RentalEntity> findRentalsByStatus(RentalStatus status) {
        return rentalJpaRepository.findRentalsByStatus(status);
    }

    @Override
    public long countVehiclesByStatus(RentalStatus status) {
        return rentalJpaRepository.countVehiclesByStatus(status);
    }
}
