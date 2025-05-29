package kernel360.ckt.auth.application.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReissueCommand {
    private String refreshToken;
}
