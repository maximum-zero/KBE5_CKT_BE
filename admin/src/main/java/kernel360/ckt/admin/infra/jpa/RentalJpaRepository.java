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
        AND (r.id != :excludeRentalId OR :excludeRentalId IS NULL)
    """)
    List<RentalEntity> findOverlappingRentalsByVehicleAndStatusesExcludingRental(
        @Param("vehicle") VehicleEntity vehicle,
        @Param("statuses") List<RentalStatus> statuses,
        @Param("pickupAt") LocalDateTime pickupAt,
        @Param("returnAt") LocalDateTime returnAt,
        @Param("excludeRentalId") Long excludeRentalId
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
        AND (:startAt IS NULL OR r.pickupAt > :startAt)
        AND (:endAt IS NULL OR r.returnAt < :endAt)
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

    @Query("SELECT COUNT(DISTINCT r.customer.id) FROM RentalEntity r WHERE r.status = 'RENTED'")
    long countRentedCustomers();

    @Query("""
    SELECT r FROM RentalEntity r
    JOIN FETCH r.vehicle
    WHERE r.customer.id = :customerId AND r.status = :status
    ORDER BY r.pickupAt DESC
""")
    List<RentalEntity> findFirstRentalByCustomerIdAndStatusFetchVehicle(
        @Param("customerId") Long customerId,
        @Param("status") RentalStatus status
    );

    @Query("""
    SELECT r FROM RentalEntity r
    JOIN FETCH r.vehicle
    WHERE r.customer.id = :customerId
    ORDER BY r.pickupAt DESC
""")
    List<RentalEntity> findAllByCustomerIdFetchVehicle(@Param("customerId") Long customerId);

    long countByCustomerId(Long customerId);

    long countByCustomerIdAndStatus(Long customerId, RentalStatus status);
}
