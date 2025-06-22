package kernel360.ckt.collector.infra;

import kernel360.ckt.collector.application.port.VehicleTraceLogRepository;
import kernel360.ckt.collector.infra.jpa.VehicleTraceLogJpaRepository;
import kernel360.ckt.core.domain.entity.VehicleTraceLogEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class VehicleTraceLogRepositoryAdapter implements VehicleTraceLogRepository {
    private final VehicleTraceLogJpaRepository vehicleTraceLogJpaRepository;

    @Override
    public VehicleTraceLogEntity save(VehicleTraceLogEntity vehicleTraceLog) {
        return vehicleTraceLogJpaRepository.save(vehicleTraceLog);
    }
}
