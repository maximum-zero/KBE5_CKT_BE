package kernel360.ckt.admin.infra;

import kernel360.ckt.admin.application.port.VehicleTraceLogRepository;
import kernel360.ckt.admin.infra.jpa.VehicleTraceLogJpaRepository;
import kernel360.ckt.core.domain.entity.VehicleTraceLogEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class TraceLogRepositoryAdapter implements VehicleTraceLogRepository {
    private final VehicleTraceLogJpaRepository vehicleTraceLogJpaRepository;

    @Override
    public List<VehicleTraceLogEntity> findByRouteId(Long routeId) {
        return vehicleTraceLogJpaRepository.findByRouteId(routeId);
    }
}
