package kernel360.ckt.admin.application.command;

import kernel360.ckt.core.domain.entity.CustomerEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateCustomerCommand {

    private final String customerName;
    private final String customerType;
    private final String phoneNumber;
    private final String licenseNumber;
    private final String zipCode;
    private final String address;
    private final String detailedAddress;
    private final String birthday;
    private final String memo;

    public CustomerEntity toEntity() {
        return CustomerEntity.create(
            this.customerName,
            this.customerType,
            this.phoneNumber,
            this.licenseNumber,
            this.zipCode,
            this.address,
            this.detailedAddress,
            this.birthday,
            this.memo
        );
    }
}
