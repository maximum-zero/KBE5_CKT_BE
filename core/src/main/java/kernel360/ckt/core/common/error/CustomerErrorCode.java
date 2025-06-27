package kernel360.ckt.core.common.error;

import lombok.Getter;

@Getter
public enum CustomerErrorCode implements ErrorCode {
    CUSTOMER_NOT_FOUND("404", "해당 고객을 찾을 수 없습니다.", 404),
    DUPLICATE_LICENSE_NUMBER("400", "이미 등록된 면허번호입니다.", 400),
    CUSTOMER_CREATION_FAILED("500", "고객 생성 중 오류가 발생했습니다.", 500),
    CUSTOMER_UPDATE_FAILED("500", "고객 수정 중 오류가 발생했습니다.", 500);

    private final String code;
    private final String message;
    private final int status;

    CustomerErrorCode(String code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
