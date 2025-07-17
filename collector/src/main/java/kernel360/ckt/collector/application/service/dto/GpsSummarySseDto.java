package kernel360.ckt.collector.application.service.dto;

public record GpsSummarySseDto(
    Long vehicleId,
    double lat,
    double lon,
    int spd,
    Boolean stolen
) {}
