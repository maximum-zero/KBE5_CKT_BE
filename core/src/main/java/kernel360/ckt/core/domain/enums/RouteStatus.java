package kernel360.ckt.core.domain.enums;

import lombok.Getter;

@Getter
public enum RouteStatus {
    ACTIVE("경로 활성"),
    COMPLETED("경로 완료"),
    PAUSED("경로 일시 정지")
    ;

    private final String value;

    RouteStatus(String value) {
        this.value = value;
    }

}

