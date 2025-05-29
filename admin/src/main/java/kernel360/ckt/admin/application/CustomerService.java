package kernel360.ckt.admin.application;

import jakarta.persistence.EntityNotFoundException;
import kernel360.ckt.admin.application.command.CreateCustomerCommand;
import kernel360.ckt.admin.ui.dto.request.CustomerUpdateRequest;
import kernel360.ckt.core.domain.entity.CustomerEntity;
import kernel360.ckt.core.domain.entity.CustomerStatus;
import kernel360.ckt.core.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerEntity create(CreateCustomerCommand command) {
        return customerRepository.save(command.toEntity());
    }

    public CustomerEntity update(Long id, CustomerUpdateRequest request) {
        CustomerEntity customer = customerRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(id + "고객이 존재하지 않습니다."));

        if (request != null) {
            customer.updateBasicInfo(
                request.customerName(),
                request.customerType(),
                request.phoneNumber(),
                request.licenseNumber(),
                request.zipCode(),
                request.status(),
                request.address(),
                request.detailedAddress(),
                request.birthday(),
                request.memo()
            );
            customer = customerRepository.save(customer);
            return customer;
        }
        return customerRepository.save(customer);
    }

    public CustomerEntity findById(Long id) {
        return customerRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(id + "고객이 존재하지 않습니다."));
    }

    public void delete(Long id) {
        customerRepository.deleteById(id);
    }

    public Page<CustomerEntity> searchCustomers(CustomerStatus status, String keyword, Pageable pageable) {
        return customerRepository.search(status, keyword, pageable);
    }

    public CustomerEntity findByLicenseNumber(String licenseNumber) {
        return customerRepository.findByLicenseNumber(licenseNumber)
            .orElseThrow(() -> new EntityNotFoundException(licenseNumber + "에 해당하는 고객이 존재하지 않습니다."));
    }

}
