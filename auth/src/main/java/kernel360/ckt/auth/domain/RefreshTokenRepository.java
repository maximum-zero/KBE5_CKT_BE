package kernel360.ckt.auth.domain;

import kernel360.ckt.auth.infra.RefreshTokenEntity;

import java.util.Optional;

public interface RefreshTokenRepository {
    Optional<RefreshTokenEntity> findByCompanyId(Long companyId);
}
