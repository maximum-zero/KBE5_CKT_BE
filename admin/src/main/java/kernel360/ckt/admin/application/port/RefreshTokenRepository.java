package kernel360.ckt.admin.application.port;

import java.util.Optional;
import kernel360.ckt.core.domain.entity.RefreshTokenEntity;

public interface RefreshTokenRepository {
    RefreshTokenEntity save(RefreshTokenEntity refreshToken);
    Optional<RefreshTokenEntity> findByToken(String token);
    Optional<RefreshTokenEntity> findByCompanyId(Long companyId);
    void delete(RefreshTokenEntity refreshToken);
}
