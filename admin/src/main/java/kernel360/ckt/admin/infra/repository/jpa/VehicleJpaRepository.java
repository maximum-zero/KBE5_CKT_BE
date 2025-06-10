package kernel360.ckt.admin.infra.repository.jpa;

import kernel360.ckt.core.domain.entity.VehicleEntity;
import kernel360.ckt.core.domain.enums.RentalStatus;
import kernel360.ckt.core.domain.enums.VehicleStatus;
import kernel360.ckt.core.repository.VehicleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehicleJpaRepository extends JpaRepository<VehicleEntity, Long>, VehicleRepository {
    @Query("""
        SELECT v FROM VehicleEntity v
        WHERE (:status IS NULL OR v.status = :status)
        AND (:keyword IS NULL OR v.registrationNumber LIKE %:keyword% OR v.modelName LIKE %:keyword%)
    """)
    Page<VehicleEntity> search(@Param("status") VehicleStatus status,
                               @Param("keyword") String keyword,
                               Pageable pageable);

    @Query("""
        SELECT v FROM VehicleEntity v
        WHERE (:keyword IS NULL OR :keyword = '' OR v.modelName LIKE %:keyword%)
        AND NOT EXISTS (
            SELECT r FROM RentalEntity r
            WHERE r.vehicle = v
            AND r.status = :excludedStatus
        )
        ORDER BY v.modelName ASC
    """)
    List<VehicleEntity> findAvailableVehicles(
        @Param("keyword") String keyword,
        @Param("excludedStatus") RentalStatus excludedStatus
    );
}
