package kernel360.ckt.collector.application.port;

import kernel360.ckt.core.domain.entity.VehicleTraceLogEntity;

/**
 * 차량 추적 로그 도메인에 대한 저장소 역할을 하는 포트 인터페이스입니다.
 * 인프라 계층(JPA, JDBC 등)에서 해당 인터페이스를 구현하여 실제 데이터 액세스를 수행합니다.
 */
public interface VehicleTraceLogRepository {
    /**
     * 차량 추적 로그를 저장하거나 수정합니다.
     *
     * @param vehicleTraceLog 저장할 차량 추적 로그 엔티티
     * @return 저장된 차량 추적 로그 엔티티
     */
    VehicleTraceLogEntity save(VehicleTraceLogEntity vehicleTraceLog);

}
