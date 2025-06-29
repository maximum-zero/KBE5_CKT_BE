package kernel360.ckt.admin.infra.jpa;

import java.util.Optional;
import kernel360.ckt.core.domain.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByToken(String token);
    Optional<RefreshTokenEntity> findByCompanyId(Long companyId);
}
