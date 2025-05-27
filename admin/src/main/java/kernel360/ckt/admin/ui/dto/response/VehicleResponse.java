package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.VehicleEntity;
import kernel360.ckt.core.domain.enums.VehicleStatus;

public record VehicleResponse(
    Long id,
    String registrationNumber,
    String modelYear,
    String manufacturer,
    String modelName,
    String batteryVoltage,
    String fuelType,
    String transmissionType,
    VehicleStatus status,
    String memo
) {
    public static VehicleResponse from(VehicleEntity vehicleEntity) {
        return new VehicleResponse(
            vehicleEntity.getId(),
            vehicleEntity.getRegistrationNumber(),
            vehicleEntity.getModelYear(),
            vehicleEntity.getManufacturer(),
            vehicleEntity.getModelName(),
            vehicleEntity.getBatteryVoltage(),
            vehicleEntity.getFuelType(),
            vehicleEntity.getTransmissionType(),
            vehicleEntity.getStatus(),
            vehicleEntity.getMemo()
        );
    }
}
