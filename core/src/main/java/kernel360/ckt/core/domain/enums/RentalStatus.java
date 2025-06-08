package kernel360.ckt.core.domain.enums;

import kernel360.ckt.core.domain.entity.RentalEntity;
import lombok.Getter;

@Getter
public enum RentalStatus {
    PENDING("예약 대기") {
        @Override
        public void update(RentalEntity rental, RentalStatus newStatus) {
            if (newStatus == RENTED || newStatus == CANCELED) {
                rental.updateStatus(newStatus);
            } else {
                throw new IllegalStateException(newStatus.getValue() + " 상태로 변경할 수 없습니다.");
            }
        }
    },
    RENTED("렌트 중") {
        @Override
        public void update(RentalEntity rental, RentalStatus newStatus) {
            if (newStatus == RETURNED) {
                rental.updateStatus(newStatus);
            } else {
                throw new IllegalStateException(newStatus.getValue() + " 상태로 변경할 수 없습니다.");
            }
        }
    },
    RETURNED("반납 완료") {
        @Override
        public void update(RentalEntity rental, RentalStatus newStatus) {
            throw new IllegalStateException("반납 완료된 상태는 변경할 수 없습니다.");
        }
    },
    CANCELED("렌트 취소") {
        @Override
        public void update(RentalEntity rental, RentalStatus newStatus) {
            throw new IllegalStateException("취소된 상태는 변경할 수 없습니다.");
        }
    };

    private final String value;

    RentalStatus(String value) {
        this.value = value;
    }

    public abstract void update(RentalEntity rental, RentalStatus newStatus);
}
