package kernel360.ckt.admin.infra.jpa;

import kernel360.ckt.core.domain.entity.CustomerEntity;
import kernel360.ckt.admin.infra.CustomerRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, Long>, CustomerRepository {

}
