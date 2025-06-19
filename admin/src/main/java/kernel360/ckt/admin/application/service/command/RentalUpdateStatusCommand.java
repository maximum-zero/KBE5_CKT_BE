package kernel360.ckt.admin.application.service.command;

import kernel360.ckt.core.domain.enums.RentalStatus;

public record RentalUpdateStatusCommand(
    Long id,
    Long companyId,
    RentalStatus status
) {
    public static RentalUpdateStatusCommand create(
        Long id,
        Long companyId,
        RentalStatus status
    ) {
        return new RentalUpdateStatusCommand(id, companyId, status);
    }
}
