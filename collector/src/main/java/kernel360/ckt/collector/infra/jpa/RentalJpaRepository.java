package kernel360.ckt.collector.infra.jpa;

import java.time.LocalDateTime;
import java.util.Optional;
import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.enums.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RentalJpaRepository extends JpaRepository<RentalEntity, Long> {
    @Query("""
        SELECT r FROM RentalEntity r
        JOIN FETCH r.vehicle v
        JOIN FETCH r.company co
        JOIN FETCH r.customer c
        WHERE r.vehicle.id = :vehicleId
          AND r.pickupAt <= :onTime
          AND r.returnAt >= :onTime
          AND r.status = :status
    """)
    Optional<RentalEntity> findActiveRental(
        @Param("vehicleId") Long vehicleId,
        @Param("onTime") LocalDateTime onTime,
        @Param("status") RentalStatus status
    );
}
