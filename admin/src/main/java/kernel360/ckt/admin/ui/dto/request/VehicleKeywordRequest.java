package kernel360.ckt.admin.ui.dto.request;

import kernel360.ckt.admin.application.command.VehicleKeywordCommand;
import org.springframework.web.bind.annotation.RequestParam;

public record VehicleKeywordRequest(
    @RequestParam("keyword") String keyword
) {
    public VehicleKeywordCommand toCommand() {
        return VehicleKeywordCommand.create(keyword);
    }
}
