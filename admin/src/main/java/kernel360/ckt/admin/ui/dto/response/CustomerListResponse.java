package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.CustomerEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public record CustomerListResponse (
    List<CustomerResponse> list,
    int page,
    int size,
    long totalElements,
    int totalPages
) {
        public static CustomerListResponse from(Page<CustomerEntity> pageData) {
            List<CustomerResponse> vehicleList = pageData.getContent()
                .stream()
                .map(CustomerResponse::from)
                .toList();

            return new CustomerListResponse(
                vehicleList,
                pageData.getNumber(),
                pageData.getSize(),
                pageData.getTotalElements(),
                pageData.getTotalPages()
            );
        }
    }
