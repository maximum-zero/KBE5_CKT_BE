package kernel360.ckt.admin.infra;

import kernel360.ckt.core.domain.entity.VehicleTraceLogEntity;

public interface VehicleTraceLogRepository {
    VehicleTraceLogEntity save(VehicleTraceLogEntity vehicleTraceLog);
}
