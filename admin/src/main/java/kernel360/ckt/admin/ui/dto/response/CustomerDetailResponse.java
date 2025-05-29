package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.CustomerEntity;

public record CustomerDetailResponse(
    Long id,
    String customerName,
    String phoneNumber,
    String licenseNumber
) {
    public static CustomerDetailResponse from(CustomerEntity entity) {
        return new CustomerDetailResponse(
            entity.getId(),
            entity.getCustomerName(),
            entity.getPhoneNumber(),
            entity.getLicenseNumber()
        );
    }
}
