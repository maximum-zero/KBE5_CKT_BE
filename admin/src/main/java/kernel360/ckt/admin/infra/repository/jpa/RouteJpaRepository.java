package kernel360.ckt.admin.infra.repository.jpa;

import kernel360.ckt.admin.ui.dto.response.VehicleLogSummaryResponse;
import kernel360.ckt.core.domain.entity.RouteEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RouteJpaRepository extends Repository<RouteEntity, Long> {

    @Query("""
    SELECT new kernel360.ckt.admin.ui.dto.response.VehicleLogSummaryResponse(
        v.registrationNumber,
        c.name,
        COUNT(DISTINCT FUNCTION('DATE', r.startAt)),
        SUM(r.totalDistance),
        AVG(r.totalDistance),
        CAST(FUNCTION('SEC_TO_TIME', AVG(FUNCTION('TIMESTAMPDIFF', SECOND, r.startAt, r.endAt))) AS string)
    )
    FROM RouteEntity r
    LEFT JOIN r.drivingLog dl
    LEFT JOIN dl.rental rent
    LEFT JOIN rent.vehicle v
    LEFT JOIN rent.company c
    LEFT JOIN rent.customer cu
    WHERE r.startAt BETWEEN :startDate AND :endDate
        AND (:registrationNumber IS NULL OR :registrationNumber = '' OR v.registrationNumber = :registrationNumber)
        AND (:driverName IS NULL OR :driverName = '' OR cu.customerName LIKE %:driverName%)
    GROUP BY v.registrationNumber, c.name
    """)
    List<VehicleLogSummaryResponse> findVehicleLogSummaryBetween(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("registrationNumber") String registrationNumber,
        @Param("driverName") String driverName
    );
}
