package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.RentalEntity;

public record RentalCreateResponse(
    Long id
) {
    public static RentalCreateResponse from(RentalEntity rental) {
        return new RentalCreateResponse(rental.getId());
    }
}
