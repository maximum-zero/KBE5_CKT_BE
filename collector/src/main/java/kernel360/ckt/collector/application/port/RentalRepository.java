package kernel360.ckt.collector.application.port;

import java.time.LocalDateTime;
import java.util.Optional;
import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.enums.RentalStatus;

/**
 * 예약 도메인에 대한 저장소 역할을 하는 포트 인터페이스입니다.
 * 인프라 계층(JPA, JDBC 등)에서 해당 인터페이스를 구현하여 실제 데이터 액세스를 수행합니다.
 */
public interface RentalRepository {

    /**
     * 특정 차량 ID와 시간, 예약 상태에 해당하는 활성화된 예약 정보를 조회합니다.
     *
     * @param vehicleId 조회할 차량 ID
     * @param onTime    조회 기준 시간 (예약 시작 시간과 비교)
     * @param status    조회할 예약 상태 (예: ACTIVE, IN_PROGRESS 등)
     * @return 존재할 경우 예약 엔티티, 존재하지 않을 경우 빈 Optional 객체
     */
    Optional<RentalEntity> findActiveRental(Long vehicleId, LocalDateTime onTime, RentalStatus status);

}
