package kernel360.ckt.admin.application;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import kernel360.ckt.admin.ui.dto.request.DrivingLogUpdateRequest;
import kernel360.ckt.admin.ui.dto.response.DrivingLogDetailResponse;
import kernel360.ckt.admin.ui.dto.response.DrivingLogListResponse;
import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RouteEntity;
import kernel360.ckt.core.domain.enums.DrivingType;
import kernel360.ckt.core.repository.DrivingLogRepository;
import kernel360.ckt.core.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
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

        Page<DrivingLogEntity> drivingLogPage = drivingLogRepository.searchDrivingLogs(
            vehicleNumber,
            userName,
            startDate,
            endDate,
            type,
            pageable
        );

        List<RouteEntity> allRoutes = routeRepository.findByDrivingLogIn(drivingLogPage.getContent());

        Map<Long, List<RouteEntity>> routeMap = allRoutes.stream()
            .collect(Collectors.groupingBy(route -> route.getDrivingLog().getId()));

        return DrivingLogListResponse.from(drivingLogPage, routeMap);
    }

    public DrivingLogDetailResponse getDrivingLogDetail(Long id) {
        DrivingLogEntity drivingLogEntity = drivingLogRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("DrivingLog not found with id: " + id));

        List<RouteEntity> routes = routeRepository.findByDrivingLogId(id);

        return DrivingLogDetailResponse.from(drivingLogEntity, routes);
    }

    public DrivingLogEntity update(Long id, DrivingLogUpdateRequest request) {
        DrivingLogEntity drivingLog = drivingLogRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("운행 기록을 찾을 수 없습니다."));

        if (request.type() != null) {
            drivingLog.setType(request.type());
        }

        if (request.memo() != null) {
            drivingLog.setMemo(request.memo());
        }
        return drivingLog;
    }
}
