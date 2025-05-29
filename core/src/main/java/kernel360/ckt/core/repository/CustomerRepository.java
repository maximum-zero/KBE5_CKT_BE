package kernel360.ckt.core.repository;

import kernel360.ckt.core.domain.entity.CustomerEntity;
import kernel360.ckt.core.domain.entity.CustomerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository {
    CustomerEntity save(CustomerEntity customerEntity);
    void deleteById(Long id);
    Page<CustomerEntity> findAll(Pageable pageable);
    Optional<CustomerEntity> findById(Long id);

    // licenseNumber로 조회하는 메서드도 이름 쿼리 방식으로 선언
    Optional<CustomerEntity> findByLicenseNumber(String licenseNumber);

    // 조회
    @Query("""
    SELECT c FROM CustomerEntity c
    WHERE (:status IS NULL OR c.status = :status)
      AND (
        :keyword IS NULL
        OR c.customerName LIKE %:keyword%
        OR c.phoneNumber LIKE %:keyword%
      )
""")
    Page<CustomerEntity> search(
        @Param("status") CustomerStatus status,
        @Param("keyword") String keyword,
        Pageable pageable
    );
}
