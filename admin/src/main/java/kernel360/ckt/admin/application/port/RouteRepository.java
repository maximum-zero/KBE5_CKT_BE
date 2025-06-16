package kernel360.ckt.admin.application.port;

import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RouteEntity;

import java.util.List;

/**
 * 주행 경로(Route) 도메인의 저장소 역할을 담당하는 포트 인터페이스입니다.
 * 주행 기록(DrivingLog)을 기준으로 경로 데이터를 저장 및 조회합니다.
 */
public interface RouteRepository {

    /**
     * 주행 경로 정보를 저장하거나 수정합니다.
     * @param route 저장할 주행 경로 엔티티
     * @return 저장된 주행 경로 엔티티
     */
    RouteEntity save(RouteEntity route);

    /**
     * 주어진 주행 기록 목록에 해당하는 경로들을 조회합니다.
     * @param drivingLogs 조회 대상 주행 기록 리스트
     * @return 주행 경로 목록
     */
    List<RouteEntity> findByDrivingLogIn(List<DrivingLogEntity> drivingLogs);

    /**
     * 특정 주행 기록 ID에 해당하는 경로 목록을 조회합니다.
     * @param drivingLogId 주행 기록 ID
     * @return 해당 주행 기록에 연결된 경로 목록
     */
    List<RouteEntity> findByDrivingLogId(Long drivingLogId);
}
