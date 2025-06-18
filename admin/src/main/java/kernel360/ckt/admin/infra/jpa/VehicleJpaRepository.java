package kernel360.ckt.admin.infra.jpa;

import kernel360.ckt.core.domain.entity.VehicleEntity;
import kernel360.ckt.core.domain.enums.VehicleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VehicleJpaRepository extends JpaRepository<VehicleEntity, Long> {
    @Query("""
        SELECT v FROM VehicleEntity v
        WHERE (:status IS NULL OR v.status = :status)
        AND (:keyword IS NULL OR v.registrationNumber LIKE %:keyword% OR v.modelName LIKE %:keyword%)
    """)
    Page<VehicleEntity> findAll(@Param("status") VehicleStatus status, @Param("keyword") String keyword, Pageable pageable);

    Optional<VehicleEntity> findByRegistrationNumber(String registrationNumber);

    @Query("""
        SELECT v
        FROM VehicleEntity v
        LEFT JOIN RentalEntity r ON v.id = r.vehicle.id
            AND r.status = 'RENTED'
            AND r.pickupAt <= :returnAt
            AND r.returnAt >= :pickupAt
        WHERE
            (LOWER(v.registrationNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
             LOWER(v.modelName) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND r.id IS NULL
    """)
    List<VehicleEntity> searchAvailableVehiclesByKeyword(
        @Param("keyword") String keyword,
        @Param("pickupAt") LocalDateTime pickupAt,
        @Param("returnAt") LocalDateTime returnAt
    );
}
