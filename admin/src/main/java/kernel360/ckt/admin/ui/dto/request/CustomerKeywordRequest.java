package kernel360.ckt.admin.ui.dto.request;

import kernel360.ckt.admin.application.service.command.CustomerKeywordCommand;

public record CustomerKeywordRequest(
    String keyword
) {
    public CustomerKeywordCommand toCommand(Long companyId) {
        return new CustomerKeywordCommand(
            companyId,
            this.keyword
        );
    }
}
