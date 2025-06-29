package kernel360.ckt.admin.infra;

import java.util.Optional;
import kernel360.ckt.admin.application.port.RefreshTokenRepository;
import kernel360.ckt.admin.infra.jpa.RefreshTokenJpaRepository;
import kernel360.ckt.core.domain.entity.RefreshTokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class RefreshTokenRepositoryAdapter implements RefreshTokenRepository {
    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public RefreshTokenEntity save(RefreshTokenEntity refreshToken) {
        return refreshTokenJpaRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshTokenEntity> findByToken(String token) {
        return refreshTokenJpaRepository.findByToken(token);
    }

    @Override
    public Optional<RefreshTokenEntity> findByCompanyId(Long companyId) {
        return refreshTokenJpaRepository.findByCompanyId(companyId);
    }

    @Override
    public void delete(RefreshTokenEntity refreshToken) {
        refreshTokenJpaRepository.delete(refreshToken);
    }
}
