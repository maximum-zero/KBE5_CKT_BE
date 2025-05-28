package kernel360.ckt.admin.ui.dto.request;

import kernel360.ckt.admin.application.command.CreateVehicleCommand;
import kernel360.ckt.core.domain.enums.VehicleStatus;

public record VehicleCreateRequest(
    String registrationNumber,
    String modelYear,
    String manufacturer,
    String modelName,
    String batteryVoltage,
    String fuelType,
    String transmissionType,
    String memo
) {
    public CreateVehicleCommand toCommand() {
        return new CreateVehicleCommand(
            this.registrationNumber,
            this.modelYear,
            this.manufacturer,
            this.modelName,
            this.batteryVoltage,
            this.fuelType,
            this.transmissionType,
            VehicleStatus.AVAILABLE,
            this.memo
        );
    }
}
