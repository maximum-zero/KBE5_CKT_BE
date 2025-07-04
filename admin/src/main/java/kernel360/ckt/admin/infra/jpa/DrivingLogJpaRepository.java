package kernel360.ckt.admin.infra.jpa;

import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RentalEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface DrivingLogJpaRepository extends JpaRepository<DrivingLogEntity, Long> {

    @Query("""
        SELECT d FROM DrivingLogEntity d
        JOIN d.vehicle v
        LEFT JOIN d.rental r
        WHERE (:vehicleNumber IS NULL OR v.registrationNumber LIKE %:vehicleNumber%)
        AND (:startDate IS NULL OR r.pickupAt >= :startDate)
        AND (:endDate IS NULL OR r.returnAt <= :endDate)
    """)
    Page<DrivingLogEntity> findAll(
        @Param("vehicleNumber") String vehicleNumber,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        Pageable pageable
    );

    Optional<DrivingLogEntity> findByRental(RentalEntity rental);
}
