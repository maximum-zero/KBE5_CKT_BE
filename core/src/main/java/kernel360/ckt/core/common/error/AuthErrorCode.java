package kernel360.ckt.core.common.error;

import lombok.Getter;

@Getter
public enum AuthErrorCode implements ErrorCode {
    INVALID_LOGIN_CREDENTIALS("401", "이메일 또는 비밀번호가 일치하지 않습니다.", 401);
    ;

    private final String code;
    private final String message;
    private final int status;

    AuthErrorCode(String code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
