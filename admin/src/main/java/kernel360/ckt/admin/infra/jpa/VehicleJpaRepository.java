package kernel360.ckt.admin.infra.jpa;

import kernel360.ckt.core.domain.entity.VehicleEntity;
import kernel360.ckt.core.domain.enums.VehicleStatus;
import kernel360.ckt.admin.infra.VehicleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VehicleJpaRepository extends JpaRepository<VehicleEntity, Long>, VehicleRepository {
    @Query("""
        SELECT v FROM VehicleEntity v
        WHERE (:status IS NULL OR v.status = :status)
        AND (:keyword IS NULL OR v.registrationNumber LIKE %:keyword% OR v.modelName LIKE %:keyword%)
    """)
    Page<VehicleEntity> search(@Param("status") VehicleStatus status,
                               @Param("keyword") String keyword,
                               Pageable pageable);
}
