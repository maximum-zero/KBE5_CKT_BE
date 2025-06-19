package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.enums.RentalStatus;

public record RentalStatusResponse(
    Long id,
    RentalStatus rentalStatus,
    String rentalStatusName
) {
    public static RentalStatusResponse from(RentalEntity rentalEntity) {
        return new RentalStatusResponse(
            rentalEntity.getId(),
            rentalEntity.getStatus(),
            rentalEntity.getStatus().getValue()
        );
    }
}
