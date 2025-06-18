package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.CustomerEntity;

public record CustomerKeywordResponse(
    Long id,
    String customerName,
    String phoneNumber
){
    public static CustomerKeywordResponse from(CustomerEntity customerEntity) {
        return new CustomerKeywordResponse(
            customerEntity.getId(),
            customerEntity.getCustomerName(),
            customerEntity.getPhoneNumber()
        );
    }
}
