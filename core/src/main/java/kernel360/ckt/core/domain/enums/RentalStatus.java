package kernel360.ckt.core.domain.enums;

import lombok.Getter;

@Getter
public enum RentalStatus {
    PENDING("대기 중"),
    RENTED("대여 중"),
    RETURNED("반납 완료")
    ;

    private final String value;

    RentalStatus(String value) {
        this.value = value;
    }

}
