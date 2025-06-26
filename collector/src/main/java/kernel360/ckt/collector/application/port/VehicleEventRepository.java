package kernel360.ckt.collector.application.port;

import kernel360.ckt.core.domain.entity.VehicleEventEntity;
import kernel360.ckt.core.domain.enums.VehicleEventType;

import java.util.Optional;

/**
 * 차량 이벤트 도메인에 대한 저장소 역할을 하는 포트 인터페이스입니다.
 * 인프라 계층(JPA, JDBC 등)에서 해당 인터페이스를 구현하여 실제 데이터 액세스를 수행합니다.
 */
public interface VehicleEventRepository {
    /**
     * 차량 이벤트를 저장합니다.
     *
     * @param vehicleEvent 저장할 차량 이벤트(ON, OFF)
     * @return 저장한 차량의 이벤트 객체
     */
    VehicleEventEntity save(VehicleEventEntity vehicleEvent);

    /**
     * 차량의 마지막 이벤트를 조회합니다.
     *
     * @param vehicleId 조회할 차량 ID
     * @return 존재할 경우 차량 이벤트 엔티티, 존재하지 않을 경우 빈 Optional 객체
     */
    Optional<VehicleEventEntity> findFirstByVehicleIdOrderByCreatedAtDesc(Long vehicleId);

}
