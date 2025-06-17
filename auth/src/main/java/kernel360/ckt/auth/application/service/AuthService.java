package kernel360.ckt.auth.application.service;

import kernel360.ckt.auth.application.service.command.LoginCommand;
import kernel360.ckt.auth.application.service.command.ReissueCommand;
import kernel360.ckt.auth.config.JwtTokenProvider;
import kernel360.ckt.core.common.error.AuthErrorCode;
import kernel360.ckt.core.common.error.TokenErrorCode;
import kernel360.ckt.core.common.exception.CustomException;
import kernel360.ckt.core.domain.enums.RefreshTokenStatus;
import kernel360.ckt.auth.infra.CompanyReadJpaRepository;
import kernel360.ckt.core.domain.entity.RefreshTokenEntity;
import kernel360.ckt.auth.infra.RefreshTokenJpaRepository;
import kernel360.ckt.auth.ui.dto.response.TokenResponse;
import kernel360.ckt.core.domain.entity.CompanyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CompanyReadJpaRepository companyRepository;
    private final RefreshTokenJpaRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public TokenResponse login(LoginCommand command) {
        CompanyEntity company = companyRepository.findByEmail(command.getEmail())
            .orElseThrow(() -> new CustomException(AuthErrorCode.INVALID_LOGIN_CREDENTIALS));

        if (!passwordEncoder.matches(command.getPassword(), company.getPassword())) {
            throw new CustomException(AuthErrorCode.INVALID_LOGIN_CREDENTIALS);
        }

        Date now = new Date();
        LocalDateTime issuedAt = LocalDateTime.now();
        LocalDateTime expireAt = issuedAt.plusDays(7);

        String accessToken = jwtTokenProvider.createAccessToken(company.getId(), now);
        String refreshToken = jwtTokenProvider.createRefreshToken(company.getId(), now);

        refreshTokenRepository.findByCompanyId(company.getId())
            .ifPresent(refreshTokenRepository::delete);

        RefreshTokenEntity token = RefreshTokenEntity.create(
            company.getId(),
            refreshToken,
            issuedAt,
            expireAt,
            RefreshTokenStatus.ACTIVE
        );
        refreshTokenRepository.save(token);

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse reissue(ReissueCommand command) {
        RefreshTokenEntity tokenEntity = refreshTokenRepository.findByToken(command.getRefreshToken())
            .orElseThrow(() -> new CustomException(TokenErrorCode.INVALID_REFRESH_TOKEN));

        if (tokenEntity.isExpired()) {
            throw new CustomException(TokenErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        CompanyEntity company = companyRepository.findById(tokenEntity.getCompanyId())
            .orElseThrow(() -> new CustomException(AuthErrorCode.INVALID_LOGIN_CREDENTIALS));

        Date now = new Date();
        LocalDateTime issuedAt = LocalDateTime.now();
        LocalDateTime expireAt = issuedAt.plusDays(7);

        String newAccessToken = jwtTokenProvider.createAccessToken(company.getId(), now);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(company.getId(), now);

        tokenEntity.updateToken(newRefreshToken, issuedAt, expireAt);
        refreshTokenRepository.save(tokenEntity);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

    public void logout(String bearerToken) {
        String refreshToken = bearerToken.replace("Bearer ", "").trim();

        RefreshTokenEntity tokenEntity = refreshTokenRepository.findByToken(refreshToken)
            .orElseThrow(() -> new CustomException(TokenErrorCode.EXPIRED_REFRESH_TOKEN));

        tokenEntity.expireToken();
        refreshTokenRepository.save(tokenEntity);
    }

    public Long extractCompanyIdFromToken(String token) {
        return jwtTokenProvider.extractCompanyId(token);
    }
}
