package kernel360.ckt.admin.application.command;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateRentalCommand {
    private final Long companyId;
    private final Long vehicleId;
    private final Long customerId;
    private final LocalDateTime pickupAt;
    private final LocalDateTime returnAt;
    private final String memo;

    public static CreateRentalCommand create(Long companyId, Long vehicleId, Long customerId, LocalDateTime pickupAt, LocalDateTime returnAt, String memo) {
        return new CreateRentalCommand(companyId, vehicleId, customerId, pickupAt, returnAt, memo);
    }

}
