package kernel360.ckt.admin.infra.repository.jpa;

import kernel360.ckt.admin.ui.dto.response.DailyVehicleLogResponse;
import kernel360.ckt.admin.ui.dto.response.VehicleLogSummaryResponse;
import kernel360.ckt.admin.ui.dto.response.WeeklyVehicleLogResponse;
import kernel360.ckt.core.domain.entity.RouteEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RouteJpaRepository extends Repository<RouteEntity, Long> {

    // 차량 운행 통계
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

    // 주간 운행 통계
    @Query("""
    SELECT new kernel360.ckt.admin.ui.dto.response.WeeklyVehicleLogResponse(
        CAST(
            MIN(FUNCTION('DATE_FORMAT', r.startAt, '%x-W%v'))
        AS string),
        MIN(FUNCTION('DATE', r.startAt)),
        MAX(FUNCTION('DATE', r.startAt)),
        SUM(r.totalDistance),
        CAST(
            FUNCTION('SEC_TO_TIME',
                SUM(FUNCTION('TIMESTAMPDIFF', SECOND, r.startAt, r.endAt))
            )
        AS string),
        COUNT(DISTINCT FUNCTION('DATE', r.startAt))
    )
    FROM RouteEntity r
    JOIN r.drivingLog dl
    JOIN dl.rental rent
    JOIN rent.vehicle v
    WHERE r.startAt BETWEEN :startDate AND :endDate
      AND v.registrationNumber = :registrationNumber
    GROUP BY FUNCTION('YEARWEEK', r.startAt, 1)
    ORDER BY MIN(FUNCTION('DATE', r.startAt))
    """)
    List<WeeklyVehicleLogResponse> findWeeklyVehicleLogSummary(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate")   LocalDateTime endDate,
        @Param("registrationNumber") String registrationNumber
    );

    // 일별 운행 통계
    @Query("""
        SELECT new kernel360.ckt.admin.ui.dto.response.DailyVehicleLogResponse(
            FUNCTION('DATE', r.startAt),
            FUNCTION('DAYNAME', r.startAt),
            SUM(r.totalDistance),
            CAST(FUNCTION('SEC_TO_TIME', SUM(FUNCTION('TIMESTAMPDIFF', SECOND, r.startAt, r.endAt))) AS string)
        )
        FROM RouteEntity r
        JOIN r.drivingLog dl
        JOIN dl.rental rent
        JOIN rent.vehicle v
        WHERE r.startAt BETWEEN :startDate AND :endDate
          AND v.registrationNumber = :registrationNumber
        GROUP BY FUNCTION('DATE', r.startAt), FUNCTION('DAYNAME', r.startAt)
        ORDER BY FUNCTION('DATE', r.startAt)
        """)
    List<DailyVehicleLogResponse> findDailyVehicleLogSummary(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("registrationNumber") String registrationNumber
    );

}
