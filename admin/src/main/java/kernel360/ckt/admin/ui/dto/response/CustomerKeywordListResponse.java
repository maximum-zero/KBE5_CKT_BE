package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.CustomerEntity;

import java.util.List;

public record CustomerKeywordListResponse(
    List<CustomerKeywordResponse> list
) {
        public static CustomerKeywordListResponse from(List<CustomerEntity> customers) {
            return new CustomerKeywordListResponse(customers
                .stream()
                .map(CustomerKeywordResponse::from)
                .toList()
            );
        }
    }
