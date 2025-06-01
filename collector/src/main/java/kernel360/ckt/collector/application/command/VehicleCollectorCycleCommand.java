package kernel360.ckt.collector.application.command;

import kernel360.ckt.core.domain.dto.CycleInformation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class VehicleCollectorCycleCommand {
    private final Long vehicleId;
    private final LocalDateTime onTime;
    private final List<CycleInformation> cList;

    public static VehicleCollectorCycleCommand create(
        Long vehicleId,
        LocalDateTime onTime,
        List<CycleInformation> cList
    ) {
        return new VehicleCollectorCycleCommand(vehicleId, onTime, cList);
    }
}
