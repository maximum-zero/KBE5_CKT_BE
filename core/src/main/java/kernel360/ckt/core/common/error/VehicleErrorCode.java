package kernel360.ckt.core.common.error;

import lombok.Getter;

@Getter
public enum VehicleErrorCode implements ErrorCode {
    DUPLICATE_REGISTRATION_NUMBER("409", "이미 등록된 차량 번호입니다.", 409)
    ;

    private final String code;
    private final String message;
    private final int status;

    VehicleErrorCode(String code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

}
