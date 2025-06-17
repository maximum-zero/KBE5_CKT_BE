package kernel360.ckt.auth.application.port;

import kernel360.ckt.core.domain.entity.RefreshTokenEntity;

import java.util.Optional;

public interface RefreshTokenRepository {
    Optional<RefreshTokenEntity> findByCompanyId(Long companyId);
}
