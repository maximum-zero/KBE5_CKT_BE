package kernel360.ckt.admin.ui;

import jakarta.validation.Valid;
import kernel360.ckt.admin.application.service.CustomerRentalQueryService;
import kernel360.ckt.admin.application.service.CustomerService;
import kernel360.ckt.admin.application.service.command.CreateCustomerCommand;
import kernel360.ckt.admin.ui.dto.request.CustomerCreateRequest;
import kernel360.ckt.admin.ui.dto.request.CustomerKeywordRequest;
import kernel360.ckt.admin.ui.dto.request.CustomerUpdateRequest;
import kernel360.ckt.admin.ui.dto.response.*;
import kernel360.ckt.core.common.response.CommonResponse;
import kernel360.ckt.core.domain.entity.CustomerEntity;
import kernel360.ckt.core.domain.enums.CustomerStatus;
import kernel360.ckt.core.domain.enums.CustomerType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private static final String X_USER_ID_HEADER = "X-User-Id";

    private final CustomerService customerService;
    private final CustomerRentalQueryService customerRentalQueryService;

    @GetMapping
    public CommonResponse<CustomerListResponse> getAllCustomers(
        @RequestHeader(X_USER_ID_HEADER) Long companyId,
        @RequestParam(required = false) CustomerStatus status,
        @RequestParam(required = false) CustomerType type,
        @RequestParam(required = false) String keyword,
        @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<CustomerEntity> customerPage = customerService.searchCustomers(companyId, type, status, keyword, pageable);
        return CommonResponse.success(CustomerListResponse.from(customerPage));
    }

    @GetMapping("/{id}")
    public CommonResponse<CustomerResponse> selectCustomer(@PathVariable Long id) {
        CustomerEntity customerEntity = customerService.findById(id);
        return CommonResponse.success(CustomerResponse.from(customerEntity));
    }

    @PostMapping
    public CommonResponse<CustomerResponse> create(@RequestHeader(X_USER_ID_HEADER) Long companyId, @Valid @RequestBody CustomerCreateRequest request) {
        CreateCustomerCommand command = request.toCommand(companyId);
        CustomerEntity customerEntity = customerService.create(command);
        return CommonResponse.success(CustomerResponse.from(customerEntity));
    }

    @PutMapping("/{id}")
    public CommonResponse<CustomerResponse> update(@PathVariable Long id, @Valid @RequestBody CustomerUpdateRequest request) {
        CustomerEntity customerEntity = customerService.update(id, request);
        return CommonResponse.success(CustomerResponse.from(customerEntity));
    }

    @DeleteMapping("/{id}")
    public CommonResponse<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return CommonResponse.success(null);
    }

    @GetMapping("/summary")
    public CommonResponse<CustomerSummaryResponse> getCustomerSummary(
        @RequestHeader(X_USER_ID_HEADER) Long companyId
    ) {
        return CommonResponse.success(customerService.getCustomerSummary(companyId));
    }

    @GetMapping("/search")
    public CommonResponse<CustomerKeywordListResponse> searchKeyword(@RequestHeader(X_USER_ID_HEADER) Long companyId, CustomerKeywordRequest request) {
        final List<CustomerEntity> customers = customerService.searchKeyword(request.toCommand(companyId));
        return CommonResponse.success(CustomerKeywordListResponse.from(customers));
    }

    @GetMapping("/{id}/rentals")
    public CommonResponse<RentalOverviewResponse> getCustomerRentalSummary(@PathVariable Long id) {
        return CommonResponse.success(customerRentalQueryService.getRentalSummary(id));
    }
}
