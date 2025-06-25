package kernel360.ckt.admin.infra.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.entity.VehicleEntity;
import kernel360.ckt.core.domain.enums.RentalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RentalJpaRepository extends JpaRepository<RentalEntity, Long> {
    @Query("""
        SELECT r FROM RentalEntity r
        WHERE r.vehicle = :vehicle
        AND r.status IN :statuses
        AND r.pickupAt < :returnAt
        AND r.returnAt > :pickupAt
    """)
    List<RentalEntity> findOverlappingRentalsByVehicleAndStatuses(
        @Param("vehicle") VehicleEntity vehicle,
        @Param("statuses") List<RentalStatus> statuses,
        @Param("pickupAt") LocalDateTime pickupAt,
        @Param("returnAt") LocalDateTime returnAt
    );

    @Query("""
        SELECT r FROM RentalEntity r
        JOIN FETCH r.customer c
        JOIN FETCH r.vehicle v
        JOIN FETCH r.company co
        WHERE r.company.id = :companyId
        AND (:status IS NULL OR r.status = :status)
        AND (:keyword IS NULL
            OR LOWER(r.customer.customerName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(r.vehicle.registrationNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
        AND (CAST(:startAt AS java.sql.Timestamp) IS NULL OR r.pickupAt > :startAt)
        AND (CAST(:endAt AS java.sql.Timestamp) IS NULL OR r.returnAt < :endAt)
        ORDER BY r.pickupAt DESC
    """)
    Page<RentalEntity> findAll(
        @Param("companyId") Long companyId,
        @Param("status") RentalStatus status,
        @Param("keyword") String keyword,
        @Param("startAt") LocalDateTime startAt,
        @Param("endAt") LocalDateTime endAt,
        Pageable pageable
    );

    @EntityGraph(attributePaths = {"company", "vehicle", "customer"})
    Optional<RentalEntity> findById(Long id);

    @Query("""
        SELECT r FROM RentalEntity r
        JOIN FETCH r.customer
        JOIN FETCH r.vehicle
        WHERE r.status = :status
    """)
    List<RentalEntity> findRentalsByStatus(@Param("status") RentalStatus status);

    @Query("""
        SELECT COUNT(DISTINCT r.vehicle.id)
        FROM RentalEntity r
        WHERE r.status = :status
    """)
    long countVehiclesByStatus(@Param("status") RentalStatus status);

    @Query(value = """
    SELECT vtl.trace_data_json
    FROM vehicle_trace_log vtl
    JOIN route r ON vtl.route_id = r.id
    JOIN driving_log dl ON r.driving_log_id = dl.id
    JOIN rental rt ON dl.rental_id = rt.id
    WHERE rt.vehicle_id = :vehicleId
    ORDER BY vtl.occurred_at DESC
    LIMIT 1
""", nativeQuery = true)
    Optional<String> findLatestTraceJsonByVehicleId(@Param("vehicleId") Long vehicleId);

    @Query("""
    SELECT r FROM RentalEntity r
    JOIN FETCH r.customer c
    JOIN FETCH r.vehicle v
    WHERE r.status = 'RENTED'
    AND r.pickupAt = (
        SELECT MAX(r2.pickupAt)
        FROM RentalEntity r2
        WHERE r2.vehicle = r.vehicle
        AND r2.status = 'RENTED'
    )
    ORDER BY r.pickupAt DESC

""")
    List<RentalEntity> findRentedRentals();
}
