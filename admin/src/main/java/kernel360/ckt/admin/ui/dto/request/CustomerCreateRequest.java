package kernel360.ckt.admin.ui.dto.request;

import kernel360.ckt.admin.application.service.command.CreateCustomerCommand;
import kernel360.ckt.core.domain.enums.CustomerStatus;
import kernel360.ckt.core.domain.enums.CustomerType;

public record CustomerCreateRequest(
    CustomerType customerType,
    String email,
    String customerName,
    String phoneNumber,
    String licenseNumber,
    String zipCode,
    CustomerStatus status,
    String address,
    String detailedAddress,
    String birthday
) {
    public CreateCustomerCommand toCommand() {
        return new CreateCustomerCommand(
            customerType,
            email,
            customerName,
            phoneNumber,
            licenseNumber,
            zipCode,
            status,
            address,
            detailedAddress,
            birthday
        );
    }
}
