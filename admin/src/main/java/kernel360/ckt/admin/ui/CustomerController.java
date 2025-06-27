package kernel360.ckt.admin.ui;

import kernel360.ckt.admin.application.service.CustomerService;
import kernel360.ckt.admin.application.service.command.CreateCustomerCommand;
import kernel360.ckt.admin.ui.dto.request.CustomerCreateRequest;
import kernel360.ckt.admin.ui.dto.request.CustomerKeywordRequest;
import kernel360.ckt.admin.ui.dto.request.CustomerUpdateRequest;
import kernel360.ckt.admin.ui.dto.response.CustomerKeywordListResponse;
import kernel360.ckt.admin.ui.dto.response.CustomerListResponse;
import kernel360.ckt.admin.ui.dto.response.CustomerResponse;
import kernel360.ckt.admin.ui.dto.response.CustomerSummaryResponse;
import kernel360.ckt.core.common.response.CommonResponse;
import kernel360.ckt.core.domain.entity.CustomerEntity;
import kernel360.ckt.core.domain.enums.CustomerStatus;
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

    private final CustomerService customerService;

    @GetMapping
    public CommonResponse<CustomerListResponse> getAllCustomers(
        @RequestParam(required = false) CustomerStatus status,
        @RequestParam(required = false) String keyword,
        @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<CustomerEntity> customerPage = customerService.searchCustomers(status, keyword, pageable);
        return CommonResponse.success(CustomerListResponse.from(customerPage));
    }

    @GetMapping("/{id}")
    public CommonResponse<CustomerResponse> selectCustomer(@PathVariable Long id) {
        CustomerEntity customerEntity = customerService.findById(id);
        return CommonResponse.success(CustomerResponse.from(customerEntity));
    }

    @PostMapping
    public CommonResponse<CustomerResponse> create(@RequestBody CustomerCreateRequest request) {
        CreateCustomerCommand command = request.toCommand();
        CustomerEntity customerEntity = customerService.create(command);
        return CommonResponse.success(CustomerResponse.from(customerEntity));
    }

    @PutMapping("/{id}")
    public CommonResponse<CustomerResponse> update(
        @PathVariable Long id,
        @RequestBody CustomerUpdateRequest request
    ) {
        CustomerEntity customerEntity = customerService.update(id, request);
        return CommonResponse.success(CustomerResponse.from(customerEntity));
    }

    @DeleteMapping("/{id}")
    public CommonResponse<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return CommonResponse.success(null);
    }

    @GetMapping("/summary")
    public CommonResponse<CustomerSummaryResponse> getCustomerSummary() {
        return CommonResponse.success(customerService.getCustomerSummary());
    }

    @GetMapping("/search")
    public CommonResponse<CustomerKeywordListResponse> searchKeyword(CustomerKeywordRequest request) {
        final List<CustomerEntity> customers = customerService.searchKeyword(request.toCommand());
        return CommonResponse.success(CustomerKeywordListResponse.from(customers));
    }
}
