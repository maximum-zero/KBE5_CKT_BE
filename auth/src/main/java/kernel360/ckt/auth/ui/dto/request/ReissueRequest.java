package kernel360.ckt.auth.ui.dto.request;

import kernel360.ckt.auth.application.service.command.ReissueCommand;
import lombok.Getter;

@Getter
public class ReissueRequest {
    private String refreshToken;

    public ReissueCommand toCommand() {
        return new ReissueCommand(refreshToken);
    }
}
