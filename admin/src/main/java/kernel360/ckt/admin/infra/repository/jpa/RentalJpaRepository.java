package kernel360.ckt.admin.infra.repository.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.entity.VehicleEntity;
import kernel360.ckt.core.domain.enums.RentalStatus;
import kernel360.ckt.core.repository.RentalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RentalJpaRepository extends JpaRepository<RentalEntity, Long>, RentalRepository {
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
        AND (:startAt IS NULL OR r.pickupAt > :startAt)
        AND (:endAt IS NULL OR r.returnAt < :endAt)
        ORDER BY r.pickupAt DESC
    """)
    Page<RentalEntity> search(
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
        SELECT COUNT(DISTINCT r.vehicle.id)
        FROM RentalEntity r
        WHERE r.status = 'RENTED'
    """)
    long countRentedVehicleIds();

}
