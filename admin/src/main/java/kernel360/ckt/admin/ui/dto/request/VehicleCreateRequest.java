package kernel360.ckt.admin.ui.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import kernel360.ckt.admin.application.service.command.CreateVehicleCommand;
import kernel360.ckt.core.domain.enums.FuelType;
import kernel360.ckt.core.domain.enums.TransmissionType;
import kernel360.ckt.core.domain.enums.VehicleStatus;

public record VehicleCreateRequest(
    @Pattern(regexp = "^\\d{2,3}[가-힣]{1}\\d{4}$", message = "올바르지 않은 차량 번호입니다.") String registrationNumber,
    @Pattern(regexp = "\\d{4}", message = "연식은 4자리 숫자여야 합니다.") String modelYear,
    @NotBlank String manufacturer,
    @NotBlank String modelName,
    @Pattern(regexp = "^\\d*$", message = "배터리 전압은 숫자만 입력 가능합니다.") String batteryVoltage,
    String fuelType,
    String transmissionType,
    @Size(max = 500, message = "메모는 500자 이하이어야 합니다.") String memo
) {
    public CreateVehicleCommand toCommand(Long companyId) {
        return new CreateVehicleCommand(
            companyId,
            this.registrationNumber,
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
