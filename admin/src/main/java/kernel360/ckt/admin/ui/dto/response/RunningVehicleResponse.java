package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.RentalEntity;

public record RunningVehicleResponse(
    Long vehicleId,
    String registrationNumber,
    String manufacturer,
    String modelName,
    String customerName
) {
    public static RunningVehicleResponse from(RentalEntity rental) {
        return new RunningVehicleResponse(
            rental.getVehicle().getId(),
            rental.getVehicle().getRegistrationNumber(),
            rental.getVehicle().getManufacturer(),
            rental.getVehicle().getModelName(),
            rental.getCustomer().getCustomerName()
        );
    }
}
