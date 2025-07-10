package kernel360.ckt.admin.application.service.command;

import kernel360.ckt.core.domain.entity.CustomerEntity;
import kernel360.ckt.core.domain.enums.CustomerStatus;
import kernel360.ckt.core.domain.enums.CustomerType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateCustomerCommand {

    private final Long companyId;
    private final CustomerType customerType;
    private final String email;
    private final String customerName;
    private final String phoneNumber;
    private final String licenseNumber;
    private final String zipCode;
    private final CustomerStatus status;
    private final String address;
    private final String detailedAddress;
    private final String birthday;

    public CustomerEntity toEntity() {
        return CustomerEntity.create(
            this.customerType,
            this.email,
            this.customerName,
            this.phoneNumber,
            this.licenseNumber,
            this.zipCode,
            this.address,
            this.detailedAddress,
            this.birthday,
            this.status,
            this.companyId
        );
    }
}
