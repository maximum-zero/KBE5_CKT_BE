package kernel360.ckt.admin.ui.dto.response;

public record RunningVehicleResponse(
    Long vehicleId,
    String registrationNumber,
    String manufacturer,
    String modelName
) {
    public static RunningVehicleResponse from(kernel360.ckt.core.domain.entity.VehicleEntity v) {
        return new RunningVehicleResponse(
            v.getId(),
            v.getRegistrationNumber(),
            v.getManufacturer(),
            v.getModelName()
        );
    }
}
