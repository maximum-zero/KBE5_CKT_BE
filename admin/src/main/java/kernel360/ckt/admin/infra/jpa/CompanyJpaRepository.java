package kernel360.ckt.admin.infra.jpa;

import java.util.Optional;
import kernel360.ckt.core.domain.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyJpaRepository extends JpaRepository<CompanyEntity, Long> {
    Optional<CompanyEntity> findByEmail(String email);
}
