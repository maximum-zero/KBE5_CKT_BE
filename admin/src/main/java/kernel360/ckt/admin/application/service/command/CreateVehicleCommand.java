package kernel360.ckt.admin.application.service.command;

import kernel360.ckt.core.domain.entity.CompanyEntity;
import kernel360.ckt.core.domain.entity.VehicleEntity;
import kernel360.ckt.core.domain.enums.FuelType;
import kernel360.ckt.core.domain.enums.TransmissionType;
import kernel360.ckt.core.domain.enums.VehicleStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateVehicleCommand {
    private final Long companyId;
    private final String registrationNumber;
    private final String modelYear;
    private final String manufacturer;
    private final String modelName;
    private final String batteryVoltage;
    private final FuelType fuelType;
    private final TransmissionType transmissionType;
    private final VehicleStatus status;
    private final String memo;

    public VehicleEntity toEntity(CompanyEntity company) {
        return VehicleEntity.create(
            company,
            this.registrationNumber,
            this.modelYear,
            this.manufacturer,
            this.modelName,
            this.batteryVoltage,
            this.fuelType,
            this.transmissionType,
            this.status,
            this.memo
        );
    }
}
