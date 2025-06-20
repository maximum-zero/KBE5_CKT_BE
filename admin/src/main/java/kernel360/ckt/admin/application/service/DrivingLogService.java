package kernel360.ckt.admin.application.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import kernel360.ckt.admin.ui.dto.request.DrivingLogUpdateRequest;
import kernel360.ckt.admin.ui.dto.response.DrivingLogDetailResponse;
import kernel360.ckt.admin.ui.dto.response.DrivingLogListResponse;
import kernel360.ckt.core.common.error.DrivingLogErrorCode;
import kernel360.ckt.core.common.exception.CustomException;
import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RouteEntity;
import kernel360.ckt.core.domain.enums.DrivingType;
import kernel360.ckt.admin.application.port.DrivingLogRepository;
import kernel360.ckt.admin.application.port.RouteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class DrivingLogService {
    private final DrivingLogRepository drivingLogRepository;
    private final RouteRepository routeRepository;

    public DrivingLogListResponse getDrivingLogList(
        String vehicleNumber,
        String userName,
        LocalDateTime startDate,
        LocalDateTime endDate,
        DrivingType type,
        Pageable pageable
    ) {

        Page<DrivingLogEntity> drivingLogPage = drivingLogRepository.findAll(
            vehicleNumber,
            userName,
            startDate,
            endDate,
            type,
            pageable
        );

        log.info("운행 일지 조회 완료 - 운행일지 조회 건수: {}", drivingLogPage.getTotalElements());

        List<RouteEntity> allRoutes = routeRepository.findByDrivingLogIn(drivingLogPage.getContent());

        Map<Long, List<RouteEntity>> routeMap = allRoutes.stream()
            .collect(Collectors.groupingBy(route -> route.getDrivingLog().getId()));

        return DrivingLogListResponse.from(drivingLogPage, routeMap);
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
