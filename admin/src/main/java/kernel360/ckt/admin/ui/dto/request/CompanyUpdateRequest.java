package kernel360.ckt.admin.ui.dto.request;

import kernel360.ckt.admin.application.service.command.UpdateCompanyCommand;

public record CompanyUpdateRequest(
    String name,
    String ceoName,
    String telNumber
) {
    public UpdateCompanyCommand toCommand() {
        return new UpdateCompanyCommand(
            this.name,
            this.ceoName,
            this.telNumber
        );
    }
}
