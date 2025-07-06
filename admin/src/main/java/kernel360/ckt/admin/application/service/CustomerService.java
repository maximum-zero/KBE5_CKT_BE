package kernel360.ckt.admin.application.service;

import jakarta.transaction.Transactional;
import kernel360.ckt.admin.application.port.CustomerRepository;
import kernel360.ckt.admin.application.port.RentalRepository;
import kernel360.ckt.admin.application.service.command.CreateCustomerCommand;
import kernel360.ckt.admin.application.service.command.CustomerKeywordCommand;
import kernel360.ckt.admin.ui.dto.request.CustomerUpdateRequest;
import kernel360.ckt.admin.ui.dto.response.CustomerSummaryResponse;
import kernel360.ckt.core.common.error.CustomerErrorCode;
import kernel360.ckt.core.common.exception.CustomException;
import kernel360.ckt.core.domain.entity.CustomerEntity;
import kernel360.ckt.core.domain.enums.CustomerStatus;
import kernel360.ckt.core.domain.enums.CustomerType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final RentalRepository rentalRepository;

    @Transactional
    public CustomerEntity create(CreateCustomerCommand command) {
        log.info("고객 생성 시도: {}", command);

        if (customerRepository.findByLicenseNumber(command.getLicenseNumber()).isPresent()) {
            throw new CustomException(CustomerErrorCode.DUPLICATE_LICENSE_NUMBER);
        }

        try {
            CustomerEntity customer = customerRepository.save(command.toEntity());
            log.info("고객 생성 성공: id={}, name={}", customer.getId(), customer.getCustomerName());
            return customer;
        } catch (Exception e) {
            throw new CustomException(CustomerErrorCode.CUSTOMER_CREATION_FAILED);
        }
    }

    @Transactional
    public CustomerEntity update(Long id, CustomerUpdateRequest request) {
        log.info("고객 수정 시도: id={}, request={}", id, request);

        CustomerEntity customer = customerRepository.findById(id)
            .orElseThrow(() -> new CustomException(CustomerErrorCode.CUSTOMER_NOT_FOUND));

        customerRepository.findByLicenseNumber(request.licenseNumber())
            .filter(existing -> !existing.getId().equals(id))
            .ifPresent(existing -> {
                throw new CustomException(CustomerErrorCode.DUPLICATE_LICENSE_NUMBER);
            });

        try {
            customer.updateBasicInfo(
                request.customerType(),
                request.email(),
                request.customerName(),
                request.phoneNumber(),
                request.licenseNumber(),
                request.zipCode(),
                request.status(),
                request.address(),
                request.detailedAddress(),
                request.birthday()
            );
            log.info("고객 수정 완료: id={}", id);
            return customer;
        } catch (Exception e) {
            throw new CustomException(CustomerErrorCode.CUSTOMER_UPDATE_FAILED);
        }
    }

    public CustomerEntity findById(Long id) {
        return customerRepository.findByIdAndDeleteYn(id, "N")
            .orElseThrow(() -> new CustomException(CustomerErrorCode.CUSTOMER_NOT_FOUND));
    }

    @Transactional
    public void delete(Long id) {
        log.info("고객 삭제 시도: id={}", id);

        CustomerEntity customer = customerRepository.findById(id)
            .orElseThrow(() -> new CustomException(CustomerErrorCode.CUSTOMER_NOT_FOUND));

        customer.markAsDeleted();
        log.info("고객 삭제 완료: id={}", id);
    }

    public Page<CustomerEntity> searchCustomers(CustomerStatus status, String keyword, Pageable pageable) {
        return customerRepository.findAll(status, keyword, pageable);
    }

    public CustomerSummaryResponse getCustomerSummary() {
        long total = customerRepository.countTotal();
        long individual = customerRepository.countByType(CustomerType.INDIVIDUAL);
        long corporate = customerRepository.countByType(CustomerType.CORPORATE);
        long renting = rentalRepository.countRentedCustomers();

        return CustomerSummaryResponse.of(total, individual, corporate, renting);
    }

    public List<CustomerEntity> searchKeyword(CustomerKeywordCommand command) {
        return customerRepository.search(command.getCompanyId(), command.getKeyword(), command.getKeyword());
    }

}
