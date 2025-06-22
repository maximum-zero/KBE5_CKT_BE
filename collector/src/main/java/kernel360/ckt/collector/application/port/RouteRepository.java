package kernel360.ckt.collector.application.port;

import java.util.Optional;
import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RouteEntity;
import kernel360.ckt.core.domain.enums.RouteStatus;

/**
 * 경로 도메인에 대한 저장소 역할을 하는 포트 인터페이스입니다.
 * 인프라 계층(JPA, JDBC 등)에서 해당 인터페이스를 구현하여 실제 데이터 액세스를 수행합니다.
 */
public interface RouteRepository {
    /**
     * 경로를 저장하거나 수정합니다.
     *
     * @param route 저장할 경로 엔티티
     * @return 저장된 경로 엔티티
     */
    RouteEntity save(RouteEntity route);

    /**
     * 특정 운행 일지와 경로 상태에 해당하는 첫 번째 경로를 시작 시간 기준 내림차순으로 조회합니다.
     *
     * @param drivingLog 조회할 운행 일지 엔티티
     * @param routeStatus 조회할 경로 상태
     * @return 존재할 경우 경로 엔티티, 존재하지 않을 경우 빈 Optional 객체
     */
    Optional<RouteEntity> findFirstByDrivingLogAndStatusOrderByStartAtDesc(DrivingLogEntity drivingLog, RouteStatus routeStatus);

}
