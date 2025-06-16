package kernel360.ckt.admin.ui.dto.request;

import kernel360.ckt.admin.application.service.command.CreateCompanyCommand;

public record CompanyCreateRequest(
    String email,
    String password,
    String name,
    String ceoName,
    String telNumber
) {
    public CreateCompanyCommand toCommand() {
        return new CreateCompanyCommand(
            this.email,
            this.password,
            this.name,
            this.ceoName,
            this.telNumber
        );
    }
}
