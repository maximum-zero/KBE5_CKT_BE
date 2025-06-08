package kernel360.ckt.collector.application.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.enums.RentalStatus;

public interface RentalRepository {

    Optional<RentalEntity> findActiveRental(Long vehicleId, LocalDateTime onTime, RentalStatus status);

}
