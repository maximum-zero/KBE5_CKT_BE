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
        WHERE  v.deleteYn = false
        AND (v.company.id = :companyId)
        AND (:status IS NULL OR v.status = :status)
        AND (:keyword IS NULL OR v.registrationNumber LIKE %:keyword% OR v.modelName LIKE %:keyword%)
        ORDER BY v.createdAt DESC
    """)
    Page<VehicleEntity> findAll(@Param("companyId") Long companyId, @Param("status") VehicleStatus status, @Param("keyword") String keyword, Pageable pageable);

    Optional<VehicleEntity> findByIdAndCompanyIdAndDeleteYnFalse(Long vehicleId, Long companyId);

    Optional<VehicleEntity> findByCompanyIdAndRegistrationNumberAndDeleteYnFalse(Long companyId, String registrationNumber);

    @Query("""
        SELECT v
        FROM VehicleEntity v
        LEFT JOIN RentalEntity r ON v.id = r.vehicle.id
            AND r.status IN ('PENDING', 'RENTED')
            AND r.pickupAt <= :returnAt
            AND r.returnAt >= :pickupAt
        WHERE v.deleteYn = false
        AND (v.company.id = :companyId)
        AND
            (LOWER(v.registrationNumber) LIKE LOWER(CONCAT(:keyword, '%')) OR
             LOWER(v.modelName) LIKE LOWER(CONCAT(:keyword, '%')))
        AND r.id IS NULL
    """)
    List<VehicleEntity> searchAvailableVehiclesByKeyword(
        @Param("companyId") Long companyId,
        @Param("keyword") String keyword,
        @Param("pickupAt") LocalDateTime pickupAt,
        @Param("returnAt") LocalDateTime returnAt
    );

    @Query("""
    SELECT COUNT(v) FROM VehicleEntity v
    WHERE v.id IN :vehicleIds
    AND NOT EXISTS (
        SELECT 1 FROM RentalEntity r
        WHERE r.vehicle = v
    )
""")
    long countStolenVehicles(@Param("vehicleIds") List<Long> vehicleIds);

    @Query("""
    SELECT v.id FROM VehicleEntity v
    WHERE v.id IN :vehicleIds
    AND NOT EXISTS (
        SELECT 1 FROM RentalEntity r
        WHERE r.vehicle = v
    )
""")
    List<Long> findStolenVehicleIds(@Param("vehicleIds") List<Long> vehicleIds);
}
