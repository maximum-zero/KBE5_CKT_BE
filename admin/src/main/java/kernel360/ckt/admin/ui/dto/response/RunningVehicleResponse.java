package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.admin.domain.projection.RunningVehicleProjection;

public record RunningVehicleResponse(
    Long vehicleId,
    String registrationNumber,
    String manufacturer,
    String modelName,
    String customerName,
    String lat,
    String lon,
    String spd
) {
    public static RunningVehicleResponse from(RunningVehicleProjection p) {
        return new RunningVehicleResponse(
            p.getVehicleId(),
            p.getRegistrationNumber(),
            p.getManufacturer(),
            p.getModelName(),
            p.getCustomerName(),
            p.getLat(),
            p.getLon(),
            p.getSpd()
        );
    }
}
