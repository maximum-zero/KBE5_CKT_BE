package kernel360.ckt.core.common.error;

import lombok.Getter;

@Getter
public enum TokenErrorCode implements ErrorCode {
    INVALID_REFRESH_TOKEN("401", "리프레시 토큰이 유효하지 않습니다.", 401),
    EXPIRED_REFRESH_TOKEN("401", "리프레시 토큰이 만료되었습니다.", 401)
    ;

    private final String code;
    private final String message;
    private final int status;

    TokenErrorCode(String code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
