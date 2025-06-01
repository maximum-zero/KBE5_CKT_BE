package kernel360.ckt.core.repository;

import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.enums.RentalStatus;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RentalRepository {
    RentalEntity save(RentalEntity rental);
    Optional<RentalEntity> findFirstByVehicleIdAndStatusAndPickupAt(Long vehicleId, RentalStatus status, LocalDateTime pickupAt);
}
