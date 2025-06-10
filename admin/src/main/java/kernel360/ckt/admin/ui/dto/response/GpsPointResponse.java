package kernel360.ckt.admin.ui.dto.response;

public record GpsPointResponse(
    String lat,
    String lon,
    String ang,
    String spd
) {}
