package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.CustomerEntity;

import java.time.LocalDateTime;

public record CustomerResponse (
    Long id,
    String customerName,
    String phoneNumber,
    String licenseNumber,
    String zipCode,
    String address,
    String detailAddress,
    String birthday,
    String status,
    String email,
    LocalDateTime createdAt,
    String customerType
) {
    public static CustomerResponse from(CustomerEntity customerEntity) {
        return new CustomerResponse(
            customerEntity.getId(),
            customerEntity.getCustomerName(),
            customerEntity.getPhoneNumber(),
            customerEntity.getLicenseNumber(),
            customerEntity.getZipCode(),
            customerEntity.getAddress(),
            customerEntity.getDetailedAddress(),
            customerEntity.getBirthday(),
            customerEntity.getStatus().name(),
            customerEntity.getEmail(),
            customerEntity.getCreatedAt(),
            customerEntity.getCustomerType().name()
        );
    }
}
