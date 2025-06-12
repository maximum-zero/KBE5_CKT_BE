package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.RentalEntity;

public record RunningVehicleResponse(
    Long vehicleId,
    String registrationNumber,
    String manufacturer,
    String modelName,
    String customerName,
    String lat,
    String lon,
    String ang,
    String spd
) {
    public static RunningVehicleResponse from(RentalEntity rental, GpsPointResponse location) {
        return new RunningVehicleResponse(
            rental.getVehicle().getId(),
            rental.getVehicle().getRegistrationNumber(),
            rental.getVehicle().getManufacturer(),
            rental.getVehicle().getModelName(),
            rental.getCustomer().getCustomerName(),
            location != null ? location.lat() : null,
            location != null ? location.lon() : null,
            location != null ? location.ang() : null,
            location != null ? location.spd() : null
        );
    }
}
