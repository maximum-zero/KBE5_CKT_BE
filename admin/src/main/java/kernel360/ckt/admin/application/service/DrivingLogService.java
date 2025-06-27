package kernel360.ckt.admin.application.service;

import jakarta.transaction.Transactional;
import kernel360.ckt.admin.application.service.command.DrivingLogListCommand;
import kernel360.ckt.admin.application.service.dto.DrivingLogListDto;
import kernel360.ckt.admin.ui.dto.request.DrivingLogUpdateRequest;
import kernel360.ckt.admin.ui.dto.response.DrivingLogDetailResponse;
import kernel360.ckt.admin.ui.dto.response.DrivingLogListResponse;
import kernel360.ckt.core.common.error.DrivingLogErrorCode;
import kernel360.ckt.core.common.exception.CustomException;
import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RouteEntity;
import kernel360.ckt.admin.application.port.DrivingLogRepository;
import kernel360.ckt.admin.application.port.RouteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class DrivingLogService {
    private final DrivingLogRepository drivingLogRepository;
    private final RouteRepository routeRepository;

    /**
     * 운행 일지 목록을 조회합니다.
     *
     * @param command 검색 조건 {@link DrivingLogListCommand} 객체
     * @param pageable  페이징 및 정렬 정보 객체
     *
     * @return 검색 조건과 페이징에 따라 조회된 운행일지 {@link DrivingLogEntity} 목록
     */
    public DrivingLogListResponse retrieveDrivingLogs(DrivingLogListCommand command, Pageable pageable) {
        final Page<DrivingLogListDto> drivingLogs = drivingLogRepository.findAll(command, pageable);
        log.info("운행 일지 조회 완료 - 운행일지 조회 건수: {}", drivingLogs.getTotalElements());
        return DrivingLogListResponse.from(drivingLogs);
    }

    public DrivingLogDetailResponse getDrivingLogDetail(Long id) {
        DrivingLogEntity drivingLogEntity = drivingLogRepository.findById(id)
            .orElseThrow(() -> new CustomException(DrivingLogErrorCode.DRIVING_LOG_NOT_FOUND));

        List<RouteEntity> routes = routeRepository.findByDrivingLogId(id);
        log.info("운행 기록에 대한 경로 {}건 조회 완료 - drivingLogId: {}", routes.size(), id);

        return DrivingLogDetailResponse.from(drivingLogEntity, routes);
    }

    public DrivingLogEntity update(Long id, DrivingLogUpdateRequest request) {
        DrivingLogEntity drivingLog = drivingLogRepository.findById(id)
            .orElseThrow(() -> new CustomException(DrivingLogErrorCode.DRIVING_LOG_NOT_FOUND));

        if (request.type() != null) {
            log.info("운행 기록 타입 변경 - drivingLogId: {}, newType: {}", id, request.type());
            drivingLog.setType(request.type());
        }

        if (request.memo() != null) {
            log.info("운행 기록 메모 변경 - drivingLogId: {}, newMemo: {}", id, request.memo());
            drivingLog.setMemo(request.memo());
        }
        log.info("운행 기록 수정 완료 - drivingLogId: {}", id);
        return drivingLog;
    }
}
