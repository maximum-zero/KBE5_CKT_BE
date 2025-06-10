package kernel360.ckt.admin.ui.dto.response;

import java.time.LocalDateTime;
import java.util.Optional;
import kernel360.ckt.core.domain.entity.CustomerEntity;
import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.entity.VehicleEntity;
import kernel360.ckt.core.domain.enums.RentalStatus;

public record RentalResponse(
    Long id,
    String customerName,
    String customerPhoneNumber,
    String vehicleRegistrationNumber,
    String vehicleModelName,
    String vehicleManufacture,
    String vehicleModelYear,
    RentalStatus rentalStatus,
    String rentalStatusName,
    LocalDateTime pickupAt,
    LocalDateTime returnAt,
    String memo
) {
    public static RentalResponse from(RentalEntity rentalEntity) {
        final String vehicleRegistrationNumber = Optional.ofNullable(rentalEntity.getVehicle())
            .map(VehicleEntity::getRegistrationNumber)
            .orElse(null);

        final String vehicleModelName = Optional.ofNullable(rentalEntity.getVehicle())
            .map(VehicleEntity::getModelName)
            .orElse(null);

        final String vehicleManufacture = Optional.ofNullable(rentalEntity.getVehicle())
            .map(VehicleEntity::getManufacturer)
            .orElse(null);

        final String vehicleModelYear = Optional.ofNullable(rentalEntity.getVehicle())
            .map(VehicleEntity::getModelYear)
            .orElse(null);

        final String customerName = Optional.ofNullable(rentalEntity.getCustomer())
            .map(CustomerEntity::getCustomerName)
            .orElse(null);

        final String customerPhoneNumber = Optional.ofNullable(rentalEntity.getCustomer())
            .map(CustomerEntity::getPhoneNumber)
            .orElse(null);

        return new RentalResponse(
            rentalEntity.getId(),
            customerName,
            customerPhoneNumber,
            vehicleRegistrationNumber,
            vehicleModelName,
            vehicleManufacture,
            vehicleModelYear,
            rentalEntity.getStatus(),
            rentalEntity.getStatus().getValue(),
            rentalEntity.getPickupAt(),
            rentalEntity.getReturnAt(),
            rentalEntity.getMemo()
        );
    }
}
