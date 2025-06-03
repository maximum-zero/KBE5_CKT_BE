package kernel360.ckt.admin.application;

import kernel360.ckt.admin.infra.repository.jpa.RouteJpaRepository;
import kernel360.ckt.admin.ui.dto.response.VehicleLogSummaryResponse;
import kernel360.ckt.admin.ui.dto.response.WeeklyVehicleLogResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogSummaryService {

    private final RouteJpaRepository routeJpaRepository;

    public List<VehicleLogSummaryResponse> getVehicleLogSummary(
        LocalDateTime from,
        LocalDateTime to,
        String registrationNumber,
        String driverName
    ) {
        return routeJpaRepository.findVehicleLogSummaryBetween(from, to, registrationNumber, driverName);
    }

    public List<WeeklyVehicleLogResponse> getWeeklyVehicleLogSummary(LocalDateTime from, LocalDateTime to, String registrationNumber) {
        return routeJpaRepository.findWeeklyVehicleLogSummary(from, to, registrationNumber);
    }
}
