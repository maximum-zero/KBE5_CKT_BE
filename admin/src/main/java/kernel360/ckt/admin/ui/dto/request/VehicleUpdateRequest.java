package kernel360.ckt.admin.ui.dto.request;

import kernel360.ckt.admin.application.command.UpdateVehicleCommand;
import kernel360.ckt.core.domain.enums.FuelType;
import kernel360.ckt.core.domain.enums.TransmissionType;
import kernel360.ckt.core.domain.enums.VehicleStatus;

public record VehicleUpdateRequest(
    String modelYear,
    String manufacturer,
    String modelName,
    String batteryVoltage,
    String fuelType,
    String transmissionType,
    String memo
) {
    public UpdateVehicleCommand toCommand() {
        return new UpdateVehicleCommand(
            this.modelYear,
            this.manufacturer,
            this.modelName,
            this.batteryVoltage,
            FuelType.valueOf(this.fuelType),
            TransmissionType.valueOf(this.transmissionType),
            VehicleStatus.AVAILABLE,
            this.memo
        );
    }
}
