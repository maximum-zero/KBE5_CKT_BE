package kernel360.ckt.admin.infra.repository.jpa;

import kernel360.ckt.core.domain.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyJpaRepository extends JpaRepository<CompanyEntity, Long> {
}
