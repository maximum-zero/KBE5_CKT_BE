package kernel360.ckt.admin.application.service.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class VehicleKeywordCommand {
    private final String keyword;
    private final LocalDateTime pickupAt;
    private final LocalDateTime returnAt;
}
