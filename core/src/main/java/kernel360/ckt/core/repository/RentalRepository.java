package kernel360.ckt.core.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.entity.VehicleEntity;
import kernel360.ckt.core.domain.enums.RentalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RentalRepository {
    RentalEntity save(RentalEntity rental);

    List<RentalEntity> findOverlappingRentalsByVehicleAndStatuses(
        VehicleEntity vehicle,
        List<RentalStatus> statuses,
        LocalDateTime pickupAt,
        LocalDateTime returnAt
    );

    Page<RentalEntity> search(
        Long companyId,
        RentalStatus status,
        String keyword,
        LocalDateTime startAt,
        LocalDateTime endAt,
        Pageable pageable
    );

    Optional<RentalEntity> findById(Long id);

    Optional<RentalEntity> findFirstByVehicleIdAndStatusAndPickupAt(Long vehicleId, RentalStatus status, LocalDateTime pickupAt);

    long countRentedVehicleIds();
}
