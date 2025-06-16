package kernel360.ckt.admin.infra.jpa;

import kernel360.ckt.core.domain.entity.CompanyEntity;
import kernel360.ckt.admin.infra.CompanyRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyJpaRepository extends JpaRepository<CompanyEntity, Long>, CompanyRepository {
}
