package kernel360.ckt.admin.application.service.command;

import kernel360.ckt.core.common.error.RentalErrorCode;
import kernel360.ckt.core.common.exception.CustomException;
import kernel360.ckt.core.domain.entity.CompanyEntity;
import kernel360.ckt.core.domain.entity.CustomerEntity;
import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.entity.VehicleEntity;

import java.time.LocalDateTime;

public record RentalUpdateCommand(
    Long id,
    Long companyId,
    Long vehicleId,
    Long customerId,
    LocalDateTime pickupAt,
    LocalDateTime returnAt,
    String memo
) {
    public RentalUpdateCommand {
        if (pickupAt == null || returnAt == null) {
            throw new CustomException(RentalErrorCode.RENTAL_NOT_TIME_RANGE);
        }

        if (pickupAt.isAfter(returnAt) || pickupAt.isEqual(returnAt)) {
            throw new CustomException(RentalErrorCode.RENTAL_INVALID_TIME_RANGE);
        }
    }

    public static RentalUpdateCommand create(Long id, Long companyId, Long vehicleId, Long customerId, LocalDateTime pickupAt, LocalDateTime returnAt, String memo) {
        return new RentalUpdateCommand(id, companyId, vehicleId, customerId, pickupAt, returnAt, memo);
    }

    public RentalEntity toRentalEntity(CompanyEntity company, VehicleEntity vehicle, CustomerEntity customer) {
        return RentalEntity.create(company, vehicle, customer, pickupAt, returnAt, memo);
    }
}
