package kernel360.ckt.collector.ui.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import kernel360.ckt.collector.application.service.command.VehicleCollectorOffCommand;
import kernel360.ckt.core.domain.dto.GCD;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record VehicleCollectorOffRequest(
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

    @NotNull(message = "차량 시동 ON 시간은 필수입력값입니다.")
    @JsonFormat(pattern = "yyMMddHHmmss")
    LocalDateTime onTime,

    @NotNull(message = "차량 시동 OFF 시간은 필수입력값입니다.")
    @JsonFormat(pattern = "yyMMddHHmmss")
    LocalDateTime offTime,

    @NotNull(message = "유효하지 않은 GPS 상태 입니다.")
    GCD gcd,

    @NotNull(message = "위도는 필수입니다.")
    @Range(min = -90000000, max = 90000000, message = "위도는 -90 ~ 90 사이의 값이여야 합니다.")
    Integer lat,

    @NotNull(message = "경도는 필수입니다.")
    @Range(min = -180000000, max = 180000000, message = "경도는 -180 ~ 180 사이의 값이여야 합니다.")
    Integer lon,

    @NotNull(message = "방향은 필수입니다.")
    @Range(min = 0, max = 365, message = "방향은 0 ~ 365 사이의 값이여야 합니다.")
    Integer ang,

    @NotNull(message = "속도는 필수입니다.")
    @Range(min = 0, max = 365, message = "속도는 0 ~ 255 km/h 사이의 값이여야 합니다.")
    Integer spd,

    @NotNull(message = "누적 주행거리는 필수입니다.")
    @Range(min = 0, max = 9999999, message = "누적 주행거리는 0 ~ 9999999 사이의 값이여야 합니다.")
    Integer sum
) {
    private static final double GPS_COORDINATE_DIVISOR = 1000000.0;

    public VehicleCollectorOffCommand toCommand() {
        final double lat = (double) this.lat / GPS_COORDINATE_DIVISOR;
        final double lon = (double) this.lon / GPS_COORDINATE_DIVISOR;

        return VehicleCollectorOffCommand.create(this.mdn, this.gcd, lat, lon, this.ang, this.spd, this.sum, this.onTime, this.offTime);
    }
}
