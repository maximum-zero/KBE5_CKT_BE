package kernel360.ckt.admin.ui.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import kernel360.ckt.admin.application.service.command.UpdateVehicleCommand;
import kernel360.ckt.core.domain.enums.FuelType;
import kernel360.ckt.core.domain.enums.TransmissionType;
import kernel360.ckt.core.domain.enums.VehicleStatus;

public record VehicleUpdateRequest(
    @Pattern(regexp = "\\d{4}", message = "연식은 4자리 숫자여야 합니다.") String modelYear,
    @NotBlank String manufacturer,
    @NotBlank String modelName,
    @PositiveOrZero String batteryVoltage,
    String fuelType,
    String transmissionType,
    @Size(max = 500, message = "메모는 500자 이하이어야 합니다.") String memo
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
