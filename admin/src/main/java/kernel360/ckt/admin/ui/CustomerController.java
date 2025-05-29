package kernel360.ckt.admin.ui;

import kernel360.ckt.admin.application.CustomerService;
import kernel360.ckt.admin.application.command.CreateCustomerCommand;
import kernel360.ckt.admin.ui.dto.request.CustomerCreateRequest;
import kernel360.ckt.admin.ui.dto.request.CustomerUpdateRequest;
import kernel360.ckt.admin.ui.dto.response.CustomerDetailResponse;
import kernel360.ckt.admin.ui.dto.response.CustomerListResponse;
import kernel360.ckt.admin.ui.dto.response.CustomerResponse;
import kernel360.ckt.core.common.response.CommonResponse;
import kernel360.ckt.core.domain.entity.CustomerEntity;
import kernel360.ckt.core.domain.entity.CustomerStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
@RestController
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    CommonResponse<CustomerResponse> create(@RequestBody CustomerCreateRequest request) {
        final CreateCustomerCommand command = request.toCommand();
        final CustomerEntity customerEntity = customerService.create(command);
        return CommonResponse.success(CustomerResponse.from(customerEntity));
    }

    @PutMapping("/{id}")
    CommonResponse<CustomerResponse> update(@PathVariable Long id, @RequestBody CustomerUpdateRequest request) {
        final CustomerEntity command = customerService.update(id, request);
        return CommonResponse.success(CustomerResponse.from(command));
    }

    @GetMapping
    CommonResponse<CustomerListResponse> getAllCustomers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) CustomerStatus status,
        @RequestParam(required = false) String keyword
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerEntity> customerPage = customerService.searchCustomers(status, keyword, pageable);
        return CommonResponse.success(CustomerListResponse.from(customerPage));
    }

    @GetMapping("/{id}")
    CommonResponse<CustomerResponse> selectCustomer(@PathVariable Long id) {
        final CustomerEntity customerEntity = customerService.findById(id);
        return CommonResponse.success(CustomerResponse.from(customerEntity));
    }

    @DeleteMapping("/{id}")
    public CommonResponse<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return CommonResponse.success(null);
    }

    @GetMapping("/{licenseNumber}")
    public CommonResponse<CustomerDetailResponse> getCustomerByLicenseNumber(@PathVariable String licenseNumber) {
        var customerEntity = customerService.findByLicenseNumber(licenseNumber);
        return CommonResponse.success(CustomerDetailResponse.from(customerEntity));
    }
}
