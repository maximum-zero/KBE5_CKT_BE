package kernel360.ckt.collector.ui.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kernel360.ckt.collector.application.service.command.VehicleCollectorCycleCommand;
import org.hibernate.validator.constraints.Range;

import java.util.List;

public record VehicleCollectorCycleRequest(
    @NotNull(message = "차량 ID는 필수입니다.")
    Long mdn,

    @NotNull(message = "터미널 ID는 필수입니다.")
    String tid,

    @NotNull(message = "제조사 ID는 필수입니다.")
    String mid,

    @NotNull(message = "패킷 버전은 필수입니다.")
    @Range(min = 0, max = 65535, message = "패킷버전은 0 ~ 65535 사이의 값이여야 합니다.")
    Integer pv,

    @NotNull(message = "디바이스 ID는 필수입니다.")
    String did,

    @NotNull(message = "주기 정보 개수는 필수입니다.")
    String cCnt,

    @Valid
    @Size(min = 1, message = "주기 정보는 최소 하나 이상을 가지고 있어야 합니다.")
    List<CListRequest> cList
) {
    public VehicleCollectorCycleCommand toCommand() {
        return VehicleCollectorCycleCommand.create(mdn, cList);
    }
}
