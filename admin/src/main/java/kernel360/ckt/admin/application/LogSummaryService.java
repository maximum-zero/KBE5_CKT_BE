package kernel360.ckt.admin.application;

import kernel360.ckt.admin.infra.repository.jpa.RouteJpaRepository;
import kernel360.ckt.admin.infra.repository.projection.DailyVehicleLogProjection;
import kernel360.ckt.admin.infra.repository.projection.WeeklyVehicleLogProjection;
import kernel360.ckt.admin.ui.dto.response.DailyVehicleLogResponse;
import kernel360.ckt.admin.ui.dto.response.VehicleLogSummaryResponse;
import kernel360.ckt.admin.ui.dto.response.WeeklyVehicleLogResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import kernel360.ckt.admin.infra.repository.projection.VehicleLogSummaryProjection;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        List<VehicleLogSummaryProjection> projections =
            routeJpaRepository.findVehicleLogSummaryBetween(from, to, registrationNumber, driverName);

        return projections.stream()
            .map(p -> new VehicleLogSummaryResponse(
                p.getRegistrationNumber(),
                p.getCompanyName(),
                p.getDrivingDays(),
                p.getTotalDistance(),
                p.getAverageDistance(),
                p.getAverageDrivingTime()
            ))
            .toList();
    }

    public List<WeeklyVehicleLogResponse> getWeeklyVehicleLogSummary(
        LocalDateTime from,
        LocalDateTime to,
        String registrationNumber
    ) {
        List<WeeklyVehicleLogProjection> projections = routeJpaRepository.findWeeklyVehicleLogSummary(from, to, registrationNumber);
        return projections.stream()
            .map(p -> new WeeklyVehicleLogResponse(
                p.getWeekNumber(),
                java.sql.Date.valueOf(p.getStartDate()),
                java.sql.Date.valueOf(p.getEndDate()),
                p.getTotalDistance().longValue(),
                p.getTotalDrivingTime(),
                p.getDrivingDays().longValue()
            ))
            .toList();
    }

    public List<DailyVehicleLogResponse> getDailyVehicleLogSummary(
        LocalDate weekStartDate,
        LocalDate weekEndDate,
        String registrationNumber
    ) {
        LocalDateTime start = weekStartDate.atStartOfDay();
        LocalDateTime end   = weekEndDate.atTime(LocalTime.MAX);

        return routeJpaRepository.findDailyVehicleLogSummary(start, end, registrationNumber).stream()
            .map(p -> new DailyVehicleLogResponse(
                java.sql.Date.valueOf(p.getDrivingDate()),
                p.getTotalDistance(),
                p.getTotalDrivingTime()
            ))
            .toList();
    }

}
