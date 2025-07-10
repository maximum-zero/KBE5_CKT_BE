package kernel360.ckt.admin.ui.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import kernel360.ckt.admin.application.service.command.CreateCustomerCommand;
import kernel360.ckt.core.domain.enums.CustomerStatus;
import kernel360.ckt.core.domain.enums.CustomerType;

public record CustomerCreateRequest(
    Long companyId,

    CustomerType customerType,

    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    String email,

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    String customerName,

    String phoneNumber,

    @NotBlank(message = "운전면허번호는 필수 입력 항목입니다.")
    String licenseNumber,

    String zipCode,
    CustomerStatus status,
    String address,
    String detailedAddress,
    String birthday
) {
    public CreateCustomerCommand toCommand(Long companyId) {
        return new CreateCustomerCommand(
            companyId,
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
