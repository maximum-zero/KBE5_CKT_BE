package kernel360.ckt.collector.application.service.command;

import kernel360.ckt.collector.ui.dto.request.CListRequest;
import kernel360.ckt.core.domain.dto.CycleInformation;

import java.util.List;

public record VehicleCollectorCycleCommand(
    Long vehicleId,
    List<CycleInformation> cList
) {
    public static VehicleCollectorCycleCommand create(
        Long vehicleId,
        List<CListRequest> cList
    ) {
        final List<CycleInformation> list = cList.stream().map(CListRequest::toDto).toList();
        return new VehicleCollectorCycleCommand(vehicleId, list);
    }
}
