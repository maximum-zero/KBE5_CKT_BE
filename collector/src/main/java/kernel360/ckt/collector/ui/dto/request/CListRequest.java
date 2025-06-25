package kernel360.ckt.collector.ui.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import kernel360.ckt.core.domain.dto.CycleInformation;
import kernel360.ckt.core.domain.dto.GCD;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

public record CListRequest(
    @NotNull(message = "유효하지 않은 GPS 상태입니다.")
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
    Integer sum,

    @NotNull(message = "배터리 전압은 필수입니다.")
    @Range(min = 0, max = 9999, message = "배터리 전압은 0 ~ 9999V 사이의 값이여야 합니다.")
    Integer bat,

    @NotNull(message = "발생시간은 필수입력값입니다.")
    @JsonFormat(pattern = "yyyyMMddHHmmss")
    LocalDateTime oTime
) {
    public CycleInformation toDto() {
        final double lat = (double) this.lat / 1_000_000.0;
        final double lon = (double) this.lon / 1_000_000.0;
        return new CycleInformation(
            this.gcd, lat, lon, this.ang, this.spd, this.sum, this.bat, this.oTime
        );
    }
}
