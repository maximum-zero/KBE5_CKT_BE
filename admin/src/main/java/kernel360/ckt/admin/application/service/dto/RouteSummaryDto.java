package kernel360.ckt.admin.application.service.dto;

import java.time.LocalDateTime;

public record RouteSummaryDto(
    Long id,
    LocalDateTime startAt,
    LocalDateTime endAt,
    Long startOdometer,
    Long endOdometer,
    Long totalDistance
) {}
