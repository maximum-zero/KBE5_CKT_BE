package kernel360.ckt.admin.application.command;

import jakarta.persistence.EntityNotFoundException;
import kernel360.ckt.admin.ui.dto.request.CustomerRequest;
import kernel360.ckt.admin.ui.dto.response.CustomerResponse;
import kernel360.ckt.core.domain.entity.CustomerEntity;
import kernel360.ckt.core.domain.entity.CustomerStatus;
import kernel360.ckt.core.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerResponse create(CustomerRequest request) {
        CustomerEntity customer = CustomerEntity.builder()
            .customerType(request.getCustomerType())
            .customerName(request.getCustomerName())
            .phoneNumber(request.getPhoneNumber())
            .licenseNumber(request.getLicenseNumber())
            .zipCode(request.getZipCode())
            .address(request.getAddress())
            .detailedAddress(request.getDetailedAddress())
            .birthday(request.getBirthday())
            .status(request.getStatus().name())
            .memo(request.getMemo())
            .build();
        return toResponse(customerRepository.save(customer));
    }

    public CustomerResponse getById(Long id) {
        CustomerEntity customer = customerRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("고객이 존재하지 않습니다."));
        return toResponse(customer);
    }

    public List<CustomerResponse> getAll() {
        return customerRepository.findAll().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public CustomerResponse update(Long id, CustomerRequest request) {
        CustomerEntity customer = customerRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("고객이 존재하지 않습니다."));

        customer.setCustomerType(request.getCustomerType());
        customer.setCustomerName(request.getCustomerName());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setLicenseNumber(request.getLicenseNumber());
        customer.setZipCode(request.getZipCode());
        customer.setAddress(request.getAddress());
        customer.setDetailedAddress(request.getDetailedAddress());
        customer.setBirthday(request.getBirthday());
        customer.setStatus(request.getStatus().name());
        customer.setMemo(request.getMemo());

        return toResponse(customerRepository.save(customer));
    }

    public void delete(Long id) {
        customerRepository.deleteById(id);
    }

    private CustomerResponse toResponse(CustomerEntity customer) {
        return new CustomerResponse(
            customer.getId(),
            customer.getCustomerName(),
            customer.getPhoneNumber(),
            CustomerStatus.valueOf(customer.getStatus())
        );
    }
}
