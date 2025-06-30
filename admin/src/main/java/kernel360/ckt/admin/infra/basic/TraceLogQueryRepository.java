package kernel360.ckt.admin.infra.basic;

import kernel360.ckt.admin.domain.projection.RunningVehicleProjection;
import kernel360.ckt.core.domain.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 시동 ON된 차량의 최신 위치 정보를 가져오는 native query repository
 */
public interface TraceLogQueryRepository extends JpaRepository<VehicleEntity, Long> {

    @Query(value = """
    SELECT
        v.id AS vehicleId,
        v.registration_number AS registrationNumber,
        v.manufacturer AS manufacturer,
        v.model_name AS modelName,
        cu.customer_name AS customerName,
        CAST(v.lat AS CHAR) AS lat,
        CAST(v.lon AS CHAR) AS lon,
        CAST(v.odometer AS CHAR) AS spd
    FROM vehicle v
    JOIN (
        SELECT ve.vehicle_id, MAX(ve.created_at) AS last_event_time
        FROM vehicle_event ve
        GROUP BY ve.vehicle_id
    ) latest_ve ON v.id = latest_ve.vehicle_id
    JOIN vehicle_event ve
      ON ve.vehicle_id = latest_ve.vehicle_id
     AND ve.created_at = latest_ve.last_event_time
     AND ve.type = 'ON'
    LEFT JOIN rental r
      ON r.vehicle_id = v.id
     AND r.status = 'RENTED'
    LEFT JOIN customer cu ON r.customer_id = cu.id
    """, nativeQuery = true)
    List<RunningVehicleProjection> findRunningVehicleLocations();
}
