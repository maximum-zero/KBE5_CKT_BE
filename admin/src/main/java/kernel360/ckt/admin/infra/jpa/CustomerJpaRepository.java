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
        WHERE (:status IS NULL OR c.status = :status)
          AND (
            :keyword IS NULL
            OR c.customerName LIKE %:keyword%
            OR c.phoneNumber LIKE %:keyword%
          )
    """)
    Page<CustomerEntity> findAll(
        @Param("status") CustomerStatus status,
        @Param("keyword") String keyword,
        Pageable pageable
    );

    Optional<CustomerEntity> findByLicenseNumber(String licenseNumber);

    // 전체 고객 수
    long count();

    // 고객 유형별 (INDIVIDUAL / CORPORATE) 수
    @Query("SELECT COUNT(c) FROM CustomerEntity c WHERE c.customerType = :type")
    long countByCustomerType(@Param("type") CustomerType type);

    List<CustomerEntity> findByCustomerNameContainingOrPhoneNumberContaining(String customerNameKeyword, String phoneNumberKeyword);
}
