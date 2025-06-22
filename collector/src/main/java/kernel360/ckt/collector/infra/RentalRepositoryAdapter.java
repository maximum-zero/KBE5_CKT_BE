package kernel360.ckt.collector.infra;

import java.time.LocalDateTime;
import java.util.Optional;
import kernel360.ckt.collector.application.port.RentalRepository;
import kernel360.ckt.collector.infra.jpa.RentalJpaRepository;
import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.enums.RentalStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class RentalRepositoryAdapter implements RentalRepository {
    private final RentalJpaRepository rentalJpaRepository;

    @Override
    public Optional<RentalEntity> findActiveRental(Long vehicleId, LocalDateTime onTime, RentalStatus status) {
        return rentalJpaRepository.findActiveRental(vehicleId, onTime, status);
    }
}
