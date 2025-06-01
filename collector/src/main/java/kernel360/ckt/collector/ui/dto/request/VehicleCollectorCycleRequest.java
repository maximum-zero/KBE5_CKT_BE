package kernel360.ckt.collector.ui.dto.request;

import kernel360.ckt.collector.application.command.VehicleCollectorCycleCommand;
import kernel360.ckt.core.domain.dto.CycleInformation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record VehicleCollectorCycleRequest(
    String mdn,
    String tid,
    String mid,
    String pv,
    String did,
    String oTime,
    String cCnt,
    List<CycleInformation> cList
) {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmss");

    public VehicleCollectorCycleCommand toCommand() {
        final LocalDateTime parsedOnTime = LocalDateTime.parse(oTime, DATE_FORMATTER);
        return VehicleCollectorCycleCommand.create(Long.parseLong(mdn), parsedOnTime, cList);
    }
}
