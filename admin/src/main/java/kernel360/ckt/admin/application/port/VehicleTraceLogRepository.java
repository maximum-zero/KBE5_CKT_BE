package kernel360.ckt.admin.application.port;

import kernel360.ckt.core.domain.entity.VehicleTraceLogEntity;

import java.util.List;

public interface VehicleTraceLogRepository {
    List<VehicleTraceLogEntity> findByRouteId(Long routeId);
}
