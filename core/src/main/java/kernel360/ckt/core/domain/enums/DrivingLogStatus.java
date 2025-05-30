package kernel360.ckt.core.domain.enums;

import lombok.Getter;

@Getter
public enum DrivingLogStatus {
    STARTED("운행 시작"),
    IN_PROGRESS("운행 중"),
    COMPLETED("운행 완료")
    ;

    private final String value;

    DrivingLogStatus(String value) {
        this.value = value;
    }

}
