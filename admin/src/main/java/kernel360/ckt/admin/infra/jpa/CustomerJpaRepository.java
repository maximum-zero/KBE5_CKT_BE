package kernel360.ckt.admin.infra.jpa;

import kernel360.ckt.core.domain.entity.CustomerEntity;
import kernel360.ckt.core.domain.enums.CustomerStatus;
import kernel360.ckt.core.domain.enums.CustomerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, Long> {
    @Query("""
    SELECT c FROM CustomerEntity c
    WHERE c.deleteYn = 'N'
      AND c.companyId = :companyId
      AND (:type IS NULL OR c.customerType = :type)
      AND (:status IS NULL OR c.status = :status)
      AND (
        :keyword IS NULL
        OR c.customerName LIKE %:keyword%
        OR c.phoneNumber LIKE %:keyword%
      )
    """)
    Page<CustomerEntity> findAll(
        @Param("companyId") Long companyId,
        @Param("type") CustomerType type,
        @Param("status") CustomerStatus status,
        @Param("keyword") String keyword,
        Pageable pageable
    );

    Optional<CustomerEntity> findByLicenseNumber(String licenseNumber);

    List<CustomerEntity> findByDeleteYnAndCompanyIdAndCustomerNameStartingWithOrPhoneNumberStartingWith(String deleteYn, Long companyId, String customerNameKeyword, String phoneNumberKeyword);

    Optional<CustomerEntity> findByIdAndDeleteYn(Long id, String deleteYn);

    long countByCompanyIdAndDeleteYn(Long companyId, String deleteYn);

    long countByCustomerTypeAndCompanyIdAndDeleteYn(CustomerType type, Long companyId, String deleteYn);
}
