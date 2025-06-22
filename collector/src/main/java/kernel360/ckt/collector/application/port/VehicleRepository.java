package kernel360.ckt.collector.application.port;

import java.util.Optional;
import kernel360.ckt.core.domain.entity.VehicleEntity;
import kernel360.ckt.core.domain.enums.VehicleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 차량 도메인에 대한 저장소 역할을 하는 포트 인터페이스입니다.
 * 인프라 계층(JPA, JDBC 등)에서 해당 인터페이스를 구현하여 실제 데이터 액세스를 수행합니다.
 */
public interface VehicleRepository {
    /**
     * 차량 ID로 차량을 조회합니다.
     *
     * @param vehicleId 조회할 차량 ID
     * @return 존재할 경우 차량 엔티티, 존재하지 않을 경우 빈 Optional 객체
     */
    Optional<VehicleEntity> findById(Long vehicleId);
}
