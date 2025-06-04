package kernel360.ckt.admin.application;

import jakarta.transaction.Transactional;
import kernel360.ckt.admin.ui.dto.response.DrivingLogListResponse;
import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RouteEntity;
import kernel360.ckt.core.repository.DrivingLogRepository;
import kernel360.ckt.core.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DrivingLogService {
    private final DrivingLogRepository drivingLogRepository;
    private final RouteRepository routeRepository;

    @Transactional
    public DrivingLogListResponse getDrivingLogList() {
        List<DrivingLogEntity> drivingLogs = drivingLogRepository.findAll();

        List<RouteEntity> allRoutes = routeRepository.findByDrivingLogIn(drivingLogs);

        Map<Long, List<RouteEntity>> routeMap = allRoutes.stream()
            .collect(Collectors.groupingBy(route -> route.getDrivingLog().getId()));

        return DrivingLogListResponse.from(drivingLogs, routeMap);
    }
}
