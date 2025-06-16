package kernel360.ckt.admin.application.service.command;

import kernel360.ckt.core.domain.enums.FuelType;
import kernel360.ckt.core.domain.enums.TransmissionType;
import kernel360.ckt.core.domain.enums.VehicleStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateVehicleCommand {
    private final String modelYear;
    private final String manufacturer;
    private final String modelName;
    private final String batteryVoltage;
    private final FuelType fuelType;
    private final TransmissionType transmissionType;
    private final VehicleStatus status;
    private final String memo;
}
