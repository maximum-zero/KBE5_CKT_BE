package kernel360.ckt.admin.infra;

import kernel360.ckt.admin.application.port.CustomerRepository;
import kernel360.ckt.admin.infra.jpa.CustomerJpaRepository;
import kernel360.ckt.core.domain.entity.CustomerEntity;
import kernel360.ckt.core.domain.enums.CustomerStatus;
import kernel360.ckt.core.domain.enums.CustomerType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CustomerRepositoryAdapter implements CustomerRepository {
    private final CustomerJpaRepository customerJpaRepository;

    @Override
    public CustomerEntity save(CustomerEntity customerEntity) {
        return customerJpaRepository.save(customerEntity);
    }

    @Override
    public Page<CustomerEntity> findAll(Long companyId, CustomerStatus status, String keyword, Pageable pageable) {
        return customerJpaRepository.findAll(companyId, status, keyword, pageable);
    }

    @Override
    public Optional<CustomerEntity> findById(Long id) {
        return customerJpaRepository.findById(id);
    }

    @Override
    public Optional<CustomerEntity> findByLicenseNumber(String licenseNumber) {
        return customerJpaRepository.findByLicenseNumber(licenseNumber);
    }

    @Override
    public List<CustomerEntity> search(Long companyId, String customerNameKeyword, String phoneNumberKeyword) {
        return customerJpaRepository.findByDeleteYnAndCompanyIdAndCustomerNameStartingWithOrPhoneNumberStartingWith("N", companyId, customerNameKeyword, phoneNumberKeyword);
    }

    @Override
    public Optional<CustomerEntity> findByIdAndDeleteYn(Long id, String deleteYn) {
        return customerJpaRepository.findByIdAndDeleteYn(id, deleteYn);
    }

    @Override
    public long countTotalByCompanyIdAndDeleteYn(Long companyId, String deleteYn) {
        return customerJpaRepository.countByCompanyIdAndDeleteYn(companyId, deleteYn);
    }

    @Override
    public long countByTypeAndCompanyIdAndDeleteYn(CustomerType type, Long companyId, String deleteYn) {
        return customerJpaRepository.countByCustomerTypeAndCompanyIdAndDeleteYn(type, companyId, deleteYn);
    }
}
