package kernel360.ckt.admin.application.service.dto;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public record DrivingLogListDto(
    Long id,
    String vehicleModelName,
    String vehicleRegistrationNumber,
    LocalDateTime startAt,
    LocalDateTime endAt,
    Long startOdometer,
    Long endOdometer,
    Long totalDistance,
    String customerName,
    List<RouteSummaryDto> routes
) {
    public DrivingLogListDto {}

    public DrivingLogListDto withAggregatedRouteData() {
        LocalDateTime minStartAt = null;
        LocalDateTime maxEndAt = null;
        Long minStartOdometer = null;
        Long maxEndOdometer = null;
        Long sumTotalDistance = 0L;

        if (this.routes != null && !this.routes.isEmpty()) {
            minStartAt = this.routes.stream()
                .map(RouteSummaryDto::startAt)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);

            maxEndAt = this.routes.stream()
                .map(RouteSummaryDto::endAt)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);

            minStartOdometer = this.routes.stream()
                .map(RouteSummaryDto::startOdometer)
                .filter(Objects::nonNull)
                .min(Comparator.naturalOrder())
                .orElse(null);

            maxEndOdometer = this.routes.stream()
                .map(RouteSummaryDto::endOdometer)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElse(null);

            sumTotalDistance = this.routes.stream()
                .map(RouteSummaryDto::totalDistance)
                .filter(Objects::nonNull)
                .mapToLong(Long::longValue)
                .sum();
        }

        return new DrivingLogListDto(
            this.id,
            this.vehicleModelName,
            this.vehicleRegistrationNumber,
            minStartAt,
            maxEndAt,
            minStartOdometer,
            maxEndOdometer,
            sumTotalDistance,
            this.customerName,
            this.routes
        );
    }
}
