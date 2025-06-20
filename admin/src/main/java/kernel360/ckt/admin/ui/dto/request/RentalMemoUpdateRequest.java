package kernel360.ckt.admin.ui.dto.request;

import kernel360.ckt.admin.application.service.command.RentalUpdateMemoCommand;

public record RentalMemoUpdateRequest(
    String memo
) {
    public RentalUpdateMemoCommand toCommand(Long id, Long companyId) {
        return RentalUpdateMemoCommand.create(
            id,
            companyId,
            memo
        );
    }
}
