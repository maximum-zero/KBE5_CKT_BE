package kernel360.ckt.core.common.error;

import lombok.Getter;

@Getter
public enum CompanyErrorCode implements ErrorCode {
    INVALID_REQUEST("400", "잘못된 요청입니다.", 400),
    COMPANY_NOT_FOUND("404", "회사를 찾을 수 없습니다.", 404)
    ;

    private final String code;
    private final String message;
    private final int status;

    CompanyErrorCode(String code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

}
