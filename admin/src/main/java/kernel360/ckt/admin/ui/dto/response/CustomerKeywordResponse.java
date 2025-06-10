package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.CustomerEntity;

import java.util.List;

public record CustomerKeywordResponse(
    List<CustomerKeywordSummaryResponse> list
) {
    public static CustomerKeywordResponse from(List<CustomerEntity> customers) {
        return new CustomerKeywordResponse(
            customers.stream().map(CustomerKeywordSummaryResponse::from).toList()
        );
    }
}
