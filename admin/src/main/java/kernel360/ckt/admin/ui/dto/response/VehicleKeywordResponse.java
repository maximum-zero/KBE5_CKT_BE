package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.VehicleEntity;

public record VehicleKeywordResponse(
    Long id,
    String registrationNumber,
    String modelName,
    String modelYear,
    String manufacturer,
    String fuelType
){
    public static VehicleKeywordResponse from(VehicleEntity vehicle) {
        return new VehicleKeywordResponse(
            vehicle.getId(),
            vehicle.getRegistrationNumber(),
            vehicle.getModelName(),
            vehicle.getModelYear(),
            vehicle.getManufacturer(),
            vehicle.getFuelType().getValue()
        );
    }
}
