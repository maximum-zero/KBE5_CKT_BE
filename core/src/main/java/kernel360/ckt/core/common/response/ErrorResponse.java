package kernel360.ckt.core.common.response;

import kernel360.ckt.core.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String code;
    private final String message;

    private ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(String.valueOf(errorCode.getCode()), errorCode.getMessage());
    }

    public static ErrorResponse from(String code, String message) {
        return new ErrorResponse(code, message);
    }

    public static ErrorResponse from(int code, String message) {
        return new ErrorResponse(String.valueOf(code), message);
    }

}
