package kernel360.ckt.collector.application.service.command;

import kernel360.ckt.core.domain.dto.CycleInformation;

import java.time.LocalDateTime;
import java.util.List;

public record VehicleCollectorCycleCommand(
    Long mdn,
    LocalDateTime onTime,
    List<CycleInformation> cList
) {
    public static VehicleCollectorCycleCommand create(
        Long vehicleId,
        LocalDateTime onTime,
        List<CycleInformation> cList
    ) {
        return new VehicleCollectorCycleCommand(vehicleId, onTime, cList);
    }
}
