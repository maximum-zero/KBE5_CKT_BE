package kernel360.ckt.core.common.error;

import lombok.Getter;

@Getter
public enum VehicleErrorCode implements ErrorCode {
    VEHICLE_NOT_FOUND("404", "차량 정보를 찾을 수 없습니다. (ID - %d)", 500),

    DUPLICATE_REGISTRATION_NUMBER("3001", "이미 등록된 차량 번호입니다.", 500)
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
