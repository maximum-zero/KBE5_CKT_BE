package kernel360.ckt.core.common.error;

import lombok.Getter;

@Getter
public enum RentalErrorCode implements ErrorCode {
    RENTAL_NOT_FOUND("404", "예약 정보를 찾을 수 없습니다. (ID - %d)", 500),

    RENTAL_VEHICLE_UNAVAILABLE("5001", "해당 차량은 지정된 시간에 예약할 수 없습니다.", 500),
    RENTAL_NOT_TIME_RANGE("5002", "예약 픽업 또는 반납 시간이 입력되지 않았습니다.", 500),
    RENTAL_INVALID_TIME_RANGE("5003", "예약 픽업 시간은 반납 시간보다 이전이어야 합니다.", 500),
    RENTAL_UPDATE_NOT_ALLOWED("5004", "%s 상태에서는 메모 항목만 변경할 수 있습니다.", 500),
    ;

    private final String code;
    private final String message;
    private final int status;

    RentalErrorCode(String code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

}
