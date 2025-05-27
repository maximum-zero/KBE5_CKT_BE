package kernel360.ckt.core.repository;

import kernel360.ckt.core.domain.entity.CustomerEntity;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    CustomerEntity save(CustomerEntity customerEntity);
    void deleteById(Long id);
    List<CustomerEntity> findAll();
    Optional<CustomerEntity> findById(Long id);
}
