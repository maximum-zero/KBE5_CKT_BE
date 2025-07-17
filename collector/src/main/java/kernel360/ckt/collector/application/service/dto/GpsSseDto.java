package kernel360.ckt.collector.application.service.dto;

public record GpsSseDto(
    Long vehicleId,
    String registrationNumber,
    String modelName,
    String manufacturer,
    double lat,
    double lon,
    int spd,
    String customerName,
    Boolean stolen
) {}
