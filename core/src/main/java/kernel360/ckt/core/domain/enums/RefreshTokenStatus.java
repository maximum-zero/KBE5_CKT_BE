package kernel360.ckt.core.domain.enums;

import lombok.Getter;

@Getter
public enum RefreshTokenStatus {
    ACTIVE("사용 중"),
    EXPIRED("만료됨");

    private final String value;

    RefreshTokenStatus(String value) {
        this.value = value;
    }
}
