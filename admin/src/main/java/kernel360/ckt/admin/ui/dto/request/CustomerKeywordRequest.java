package kernel360.ckt.admin.ui.dto.request;

import kernel360.ckt.admin.application.service.command.CustomerKeywordCommand;

public record CustomerKeywordRequest(
    String keyword
) {
    public CustomerKeywordCommand toCommand() {
        return new CustomerKeywordCommand(
            this.keyword
        );
    }
}
