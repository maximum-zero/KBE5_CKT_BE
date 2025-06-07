package kernel360.ckt.admin.application;

import jakarta.transaction.Transactional;
import kernel360.ckt.admin.ui.dto.response.DrivingLogDetailResponse;
import kernel360.ckt.admin.ui.dto.response.DrivingLogListResponse;
import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RouteEntity;
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
        LocalDate startDate,
        LocalDate endDate,
        Pageable pageable
    ) {
        ZoneId zone = ZoneId.of("Asia/Seoul");

        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;

        if (startDate != null) {
            startDateTime = startDate.atStartOfDay(zone)
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();
        }

        if (endDate != null) {
            endDateTime = endDate.atTime(LocalTime.MAX)
                .atZone(zone)
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();
        }

        Page<DrivingLogEntity> drivingLogPage = drivingLogRepository.searchDrivingLogs(
            vehicleNumber,
            userName,
            startDateTime,
            endDateTime,
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
}
