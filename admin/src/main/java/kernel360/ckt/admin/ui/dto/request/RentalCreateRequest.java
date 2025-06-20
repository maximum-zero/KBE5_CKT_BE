package kernel360.ckt.admin.ui.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import kernel360.ckt.admin.application.service.command.CreateRentalCommand;

public record RentalCreateRequest(
    @NotNull(message = "차량은 필수입니다.")
    Long vehicleId,

    @NotNull(message = "고객은 필수입니다.")
    Long customerId,

    @NotNull(message = "픽업 시간은 필수입니다.")
    @FutureOrPresent(message = "픽업 시간은 현재보다 미래여야 합니다.")
    LocalDateTime pickupAt,

    @NotNull(message = "반납 시간은 필수입니다.")
    @FutureOrPresent(message = "반납 시간은 현재보다 미래여야 합니다.")
    LocalDateTime returnAt,

    String memo
) {
    public CreateRentalCommand toCommand(Long companyId) {
        return CreateRentalCommand.create(
            companyId,
            vehicleId,
            customerId,
            pickupAt,
            returnAt,
            memo
        );
    }
}
