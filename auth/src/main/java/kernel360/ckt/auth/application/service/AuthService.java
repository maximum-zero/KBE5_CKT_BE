package kernel360.ckt.auth.application.service;

import kernel360.ckt.auth.application.service.command.LoginCommand;
import kernel360.ckt.auth.application.service.command.ReissueCommand;
import kernel360.ckt.auth.config.JwtTokenProvider;
import kernel360.ckt.core.domain.enums.RefreshTokenStatus;
import kernel360.ckt.auth.infra.CompanyReadJpaRepository;
import kernel360.ckt.core.domain.entity.RefreshTokenEntity;
import kernel360.ckt.auth.infra.RefreshTokenJpaRepository;
import kernel360.ckt.auth.ui.dto.response.TokenResponse;
import kernel360.ckt.core.domain.entity.CompanyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
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
            .orElseThrow(() -> new BadCredentialsException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(command.getPassword(), company.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
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
            .orElseThrow(() -> new IllegalArgumentException("리프레시 토큰이 유효하지 않습니다."));

        if (tokenEntity.isExpired()) {
            throw new IllegalArgumentException("리프레시 토큰이 만료되었습니다.");
        }

        CompanyEntity company = companyRepository.findById(tokenEntity.getCompanyId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

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
            .orElseThrow(() -> new IllegalArgumentException("리프레시 토큰이 유효하지 않습니다."));

        tokenEntity.expireToken();
        refreshTokenRepository.save(tokenEntity);
    }

    public Long extractCompanyIdFromToken(String token) {
        return jwtTokenProvider.extractCompanyId(token);
    }
}
