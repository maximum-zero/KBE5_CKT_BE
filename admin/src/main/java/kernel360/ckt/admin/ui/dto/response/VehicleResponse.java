package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.VehicleEntity;
import kernel360.ckt.core.domain.enums.FuelType;
import kernel360.ckt.core.domain.enums.TransmissionType;
import kernel360.ckt.core.domain.enums.VehicleStatus;

public record VehicleResponse(
    Long id,
    String registrationNumber,
    String modelYear,
    String manufacturer,
    String modelName,
    String batteryVoltage,
    FuelType fuelType,
    String fuelTypeName,
    TransmissionType transmissionType,
    String transmissionTypeName,
    VehicleStatus status,
    String statusName,
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
            vehicleEntity.getFuelType().getValue(),
            vehicleEntity.getTransmissionType(),
            vehicleEntity.getTransmissionType().getValue(),
            vehicleEntity.getStatus(),
            vehicleEntity.getStatus().getDescription(),
            vehicleEntity.getMemo()
        );
    }
}
