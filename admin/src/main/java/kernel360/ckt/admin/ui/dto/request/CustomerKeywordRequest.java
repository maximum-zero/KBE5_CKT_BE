package kernel360.ckt.admin.ui.dto.request;

import kernel360.ckt.admin.application.command.CustomerKeywordCommand;
import org.springframework.web.bind.annotation.RequestParam;

public record CustomerKeywordRequest(
    @RequestParam("keyword") String keyword
) {
    public CustomerKeywordCommand toCommand() {
        return CustomerKeywordCommand.create(keyword);
    }
}
