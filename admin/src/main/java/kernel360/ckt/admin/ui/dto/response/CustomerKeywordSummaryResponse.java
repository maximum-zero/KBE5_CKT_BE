package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.CustomerEntity;

public record CustomerKeywordSummaryResponse(
    Long id,
    String customerName,
    String phoneNumber
) {
    public static CustomerKeywordSummaryResponse from(CustomerEntity customerEntity) {
        return new CustomerKeywordSummaryResponse(
            customerEntity.getId(),
            customerEntity.getCustomerName(),
            customerEntity.getPhoneNumber()
        );
    }
}
