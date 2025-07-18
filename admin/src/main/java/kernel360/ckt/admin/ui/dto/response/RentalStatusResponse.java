package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.enums.RentalStatus;

import java.time.LocalDateTime;

public record RentalStatusResponse(
    Long id,
    RentalStatus rentalStatus,
    String rentalStatusName,
    LocalDateTime pickupAt,
    LocalDateTime returnAt
) {
    public static RentalStatusResponse from(RentalEntity rentalEntity) {
        return new RentalStatusResponse(
            rentalEntity.getId(),
            rentalEntity.getStatus(),
            rentalEntity.getStatus().getValue(),
            rentalEntity.getPickupAt(),
            rentalEntity.getReturnAt()
        );
    }
}
