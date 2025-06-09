package kernel360.ckt.admin.infra.repository.jpa;

import kernel360.ckt.admin.infra.repository.projection.DailyVehicleLogProjection;
import kernel360.ckt.admin.infra.repository.projection.VehicleLogSummaryProjection;
import kernel360.ckt.admin.infra.repository.projection.WeeklyVehicleLogProjection;
import kernel360.ckt.core.domain.entity.RouteEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RouteJpaRepository extends Repository<RouteEntity, Long> {

    // 차량 운행 통계
    @Query(
        value = """
        SELECT
            v.registration_number AS registrationNumber,
            c.name AS companyName,
            COUNT(DISTINCT DATE(r.start_at)) AS drivingDays,
            SUM(r.total_distance) AS totalDistance,
            AVG(r.total_distance) AS averageDistance,
            SEC_TO_TIME(AVG(TIMESTAMPDIFF(SECOND, r.start_at, r.end_at))) AS averageDrivingTime
        FROM route r
        LEFT JOIN driving_log dl ON r.driving_log_id = dl.id
        LEFT JOIN rental rent ON dl.rental_id = rent.id
        LEFT JOIN vehicle v ON rent.vehicle_id = v.id
        LEFT JOIN company c ON rent.company_id = c.id
        LEFT JOIN customer cu ON rent.customer_id = cu.id
        WHERE r.start_at BETWEEN :startDate AND :endDate
          AND (:registrationNumber IS NULL OR :registrationNumber = '' OR v.registration_number = :registrationNumber)
          AND (:driverName IS NULL OR :driverName = '' OR cu.customer_name LIKE CONCAT('%', :driverName, '%'))
        GROUP BY v.registration_number, c.name
        """,
        nativeQuery = true
    )
    List<VehicleLogSummaryProjection> findVehicleLogSummaryBetween(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("registrationNumber") String registrationNumber,
        @Param("driverName") String driverName
    );

    // 주간 운행 통계
    @Query(
        value = """
        SELECT
            DATE_FORMAT(MIN(r.start_at), '%x-W%v') AS weekNumber,
            MIN(DATE(r.start_at)) AS startDate,
            MAX(DATE(r.start_at)) AS endDate,
            SUM(r.total_distance) AS totalDistance,
            SEC_TO_TIME(SUM(TIMESTAMPDIFF(SECOND, r.start_at, r.end_at))) AS totalDrivingTime,
            COUNT(DISTINCT DATE(r.start_at)) AS drivingDays
        FROM route r
        JOIN driving_log dl ON r.driving_log_id = dl.id
        JOIN rental rent ON dl.rental_id = rent.id
        JOIN vehicle v ON rent.vehicle_id = v.id
        WHERE r.start_at BETWEEN :startDate AND :endDate
          AND v.registration_number = :registrationNumber
        GROUP BY YEARWEEK(r.start_at, 1)
        ORDER BY startDate
        """,
        nativeQuery = true
    )
    List<WeeklyVehicleLogProjection> findWeeklyVehicleLogSummary(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("registrationNumber") String registrationNumber
    );
    // 일별 운행 통계
    @Query(value = """
    SELECT
        DATE(r.start_at) AS drivingDate,
        SUM(r.total_distance) AS totalDistance,
        SEC_TO_TIME(SUM(TIMESTAMPDIFF(SECOND, r.start_at, r.end_at))) AS totalDrivingTime
    FROM route r
    JOIN driving_log dl ON r.driving_log_id = dl.id
    JOIN rental rent ON dl.rental_id = rent.id
    JOIN vehicle v ON rent.vehicle_id = v.id
    WHERE r.start_at BETWEEN :startDate AND :endDate
      AND v.registration_number = :registrationNumber
    GROUP BY DATE(r.start_at)
    ORDER BY drivingDate
""", nativeQuery = true)
    List<DailyVehicleLogProjection> findDailyVehicleLogSummary(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("registrationNumber") String registrationNumber
    );

}
