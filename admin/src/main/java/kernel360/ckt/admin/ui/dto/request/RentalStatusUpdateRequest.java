package kernel360.ckt.admin.ui.dto.request;

import jakarta.validation.constraints.NotNull;
import kernel360.ckt.admin.application.service.command.RentalUpdateStatusCommand;
import kernel360.ckt.core.domain.enums.RentalStatus;

public record RentalStatusUpdateRequest(
    @NotNull(message = "변경할 상태는 필수입니다.")
    RentalStatus status
) {
    public RentalUpdateStatusCommand toCommand(Long id, Long companyId) {
        return RentalUpdateStatusCommand.create(id, companyId, this.status);
    }
}
