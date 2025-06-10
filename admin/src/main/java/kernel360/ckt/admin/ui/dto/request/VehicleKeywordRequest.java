package kernel360.ckt.admin.ui.dto.request;

import kernel360.ckt.admin.application.command.VehicleKeywordCommand;

public record VehicleKeywordRequest(
    String keyword
) {
    public VehicleKeywordCommand toCommand() {
        return VehicleKeywordCommand.create(keyword);
    }
}
