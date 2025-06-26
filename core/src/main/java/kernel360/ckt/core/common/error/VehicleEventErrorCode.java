package kernel360.ckt.core.common.error;

import lombok.Getter;

@Getter
public enum VehicleEventErrorCode implements ErrorCode {
    ALREADY_RUNNING("9001", "이미 운행 중인 차량입니다. (ID - %d)", 500),
    NOT_RUNNING("9002", "운행이 멈춘 차량입니다. (ID - %d)", 500)
    ;

    private final String code;
    private final String message;
    private final int status;

    VehicleEventErrorCode(String code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

}
