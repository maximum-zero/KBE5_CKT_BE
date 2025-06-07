package kernel360.ckt.admin.infra.repository.jpa;

import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.repository.DrivingLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;


public interface DrivingLogJpaRepository extends JpaRepository<DrivingLogEntity, Long>, DrivingLogRepository {

    @Query("""
        SELECT d FROM DrivingLogEntity d
        JOIN d.vehicle v
        LEFT JOIN d.rental r
        LEFT JOIN r.customer c
        WHERE (:vehicleNumber IS NULL OR v.registrationNumber LIKE %:vehicleNumber%)
        AND (:userName IS NULL OR c.customerName LIKE %:userName%)
        AND (:startDate IS NULL OR r.pickupAt >= :startDate)
        AND (:endDate IS NULL OR r.returnAt <= :endDate)
    """)
    Page<DrivingLogEntity> searchDrivingLogs(
        @Param("vehicleNumber") String vehicleNumber,
        @Param("userName") String userName,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        Pageable pageable
    );
}
