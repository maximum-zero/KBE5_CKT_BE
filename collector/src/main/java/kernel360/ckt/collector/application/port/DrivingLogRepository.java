package kernel360.ckt.collector.application.port;

import java.util.Optional;
import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.entity.VehicleEntity;
import kernel360.ckt.core.domain.enums.DrivingLogStatus;

/**
 * 운행 일지 도메인에 대한 저장소 역할을 하는 포트 인터페이스입니다.
 * 인프라 계층(JPA, JDBC 등)에서 해당 인터페이스를 구현하여 실제 데이터 액세스를 수행합니다.
 */
public interface DrivingLogRepository {
    /**
     * 운행 일지를 저장합니다.
     *
     * @param drivingLog 저장할 운행 일지 엔티티
     * @return 저장된 운행 일지 엔티티
     */
    DrivingLogEntity save(DrivingLogEntity drivingLog);

    /**
     * 특정 렌탈 정보와 주행 상태에 해당하는 첫 번째 운행 일지를 조회합니다.
     *
     * @param rental 조회할 렌탈 엔티티
     * @param status 조회할 운행 일지 상태
     * @return 존재할 경우 운행 일지 엔티티, 존재하지 않을 경우 빈 Optional 객체
     */
    Optional<DrivingLogEntity> findFirstByRentalAndStatus(RentalEntity rental, DrivingLogStatus status);

    /**
     * 특정 차량 정보와 주행 상태에 해당하는 첫 번째 운행 일지를 생성 시간 기준 내림차순으로 조회합니다.
     *
     * @param vehicle 조회할 차량 엔티티
     * @param status 조회할 운행 일지 상태
     * @return 존재할 경우 운행 일지 엔티티, 존재하지 않을 경우 빈 Optional 객체
     */
    Optional<DrivingLogEntity> findFirstByVehicleAndStatusOrderByCreatedAtDesc(VehicleEntity vehicle, DrivingLogStatus status);

}
