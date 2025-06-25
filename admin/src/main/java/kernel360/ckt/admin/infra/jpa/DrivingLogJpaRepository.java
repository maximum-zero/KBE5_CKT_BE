package kernel360.ckt.admin.infra.jpa;

import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.enums.DrivingType;
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
        LEFT JOIN r.customer c
        WHERE (:vehicleNumber IS NULL OR v.registrationNumber LIKE %:vehicleNumber%)
        AND (:userName IS NULL OR c.customerName LIKE %:userName%)
        AND (CAST(:startDate AS java.sql.Timestamp) IS NULL OR r.pickupAt > :startDate)
        AND (CAST(:endDate AS java.sql.Timestamp) IS NULL OR r.returnAt < :endDate)
        AND (:type IS NULL OR d.type = :type)
    """)
    Page<DrivingLogEntity> findAll(
        @Param("vehicleNumber") String vehicleNumber,
        @Param("userName") String userName,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("type") DrivingType type,
        Pageable pageable
    );

    Optional<DrivingLogEntity> findByRental(RentalEntity rental);
}
