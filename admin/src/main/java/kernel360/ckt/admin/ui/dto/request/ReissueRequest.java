package kernel360.ckt.admin.ui.dto.request;

import kernel360.ckt.admin.application.service.command.ReissueCommand;
import lombok.Getter;

@Getter
public class ReissueRequest {
    private String refreshToken;

    public ReissueCommand toCommand() {
        return new ReissueCommand(refreshToken);
    }
}
