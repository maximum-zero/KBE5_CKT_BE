package kernel360.ckt.admin.infra.repository.jpa;

import kernel360.ckt.core.domain.entity.CustomerEntity;
import kernel360.ckt.core.repository.CustomerRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, Long>, CustomerRepository {

}
