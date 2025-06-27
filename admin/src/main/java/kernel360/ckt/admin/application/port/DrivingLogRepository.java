package kernel360.ckt.admin.application.port;

import kernel360.ckt.admin.application.service.command.DrivingLogListCommand;
import kernel360.ckt.admin.application.service.dto.DrivingLogListDto;
import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RentalEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * 운행 기록(DrivingLog)에 대한 저장소 역할을 하는 포트 인터페이스입니다.
 * 인프라 계층에서 해당 인터페이스를 구현하여 JPA 또는 다른 방식으로 데이터 접근을 수행합니다.
 */
public interface DrivingLogRepository {

    /**
     * 운행 기록을 저장하거나 수정합니다.
     *
     * @param drivingLog 저장할 운행 기록 엔티티
     * @return 저장된 운행 기록 엔티티
     */
    DrivingLogEntity save(DrivingLogEntity drivingLog);

    /**
     * 조건에 따라 운행 기록을 페이징 조회합니다.
     *
     * @param command 검색 조건 {@link DrivingLogListCommand} 객체
     * @param pageable 페이징 정보
     * @return 조건에 맞는 운행 기록 목록
     */
    Page<DrivingLogListDto> findAll(DrivingLogListCommand command, Pageable pageable);

    /**
     * 운행 기록 ID로 조회합니다.
     *
     * @param drivingLogId 조회할 운행 기록 ID
     * @return 존재할 경우 운행 기록 엔티티, 존재하지 않을 경우 빈 Optional 객체
     */
    Optional<DrivingLogEntity> findById(Long drivingLogId);

    /**
     * 해당 대여 정보를 기반으로 운행 기록을 조회합니다.
     *
     * @param rental 대여 엔티티
     * @return 존재할 경우 운행 기록 엔티티, 존재하지 않을 경우 빈 Optional 객체
     */
    Optional<DrivingLogEntity> findByRental(RentalEntity rental);
}
