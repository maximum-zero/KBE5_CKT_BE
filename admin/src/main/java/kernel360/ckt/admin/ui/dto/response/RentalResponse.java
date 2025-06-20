package kernel360.ckt.admin.ui.dto.response;

import java.time.LocalDateTime;
import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.enums.RentalStatus;

public record RentalResponse(
    Long id,
    RentalStatus rentalStatus,
    String rentalStatusName,
    LocalDateTime pickupAt,
    LocalDateTime returnAt,
    VehicleKeywordResponse vehicle,
    CustomerKeywordResponse customer,
    String memo
) {
    public static RentalResponse from(RentalEntity rentalEntity) {
        return new RentalResponse(
            rentalEntity.getId(),
            rentalEntity.getStatus(),
            rentalEntity.getStatus().getValue(),
            rentalEntity.getPickupAt(),
            rentalEntity.getReturnAt(),
            VehicleKeywordResponse.from(rentalEntity.getVehicle()),
            CustomerKeywordResponse.from(rentalEntity.getCustomer()),
            rentalEntity.getMemo()
        );
    }
}
