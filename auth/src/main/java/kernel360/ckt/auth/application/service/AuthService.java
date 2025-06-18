package kernel360.ckt.auth.application.service;
import kernel360.ckt.core.common.util.MaskingUtil;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final CompanyReadJpaRepository companyRepository;
    private final RefreshTokenJpaRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public TokenResponse login(LoginCommand command) {
        log.info("[로그인 시도] 이메일: {}", MaskingUtil.maskEmail(command.getEmail()));
        CompanyEntity company = companyRepository.findByEmail(command.getEmail())
            .orElseThrow(() -> {
                return new CustomException(AuthErrorCode.INVALID_LOGIN_CREDENTIALS);
            });

        if (!passwordEncoder.matches(command.getPassword(), company.getPassword())) {
            throw new CustomException(AuthErrorCode.INVALID_LOGIN_CREDENTIALS);
        }

        log.info("[로그인 성공] 회사 ID={}", company.getId());
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
        log.info("[토큰 재발급 시도] 리프레시 토큰 앞자리: {}", command.getRefreshToken().substring(0, 8));
        RefreshTokenEntity tokenEntity = refreshTokenRepository.findByToken(command.getRefreshToken())
            .orElseThrow(() -> new CustomException(TokenErrorCode.INVALID_REFRESH_TOKEN));

        if (tokenEntity.isExpired()) {
            log.info("[토큰 재발급 실패] 리프레시 토큰 만료됨");
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

        log.info("[토큰 재발급 성공] 회사 ID={}", company.getId());
        return new TokenResponse(newAccessToken, newRefreshToken);
    }

    public void logout(String bearerToken) {
        String refreshToken = bearerToken.replace("Bearer ", "").trim();
        log.info("[로그아웃 시도] 리프레시 토큰 앞자리: {}", refreshToken.substring(0, 8));

        RefreshTokenEntity tokenEntity = refreshTokenRepository.findByToken(refreshToken)
            .orElseThrow(() -> {
                log.warn("[로그아웃 실패] 존재하지 않는 리프레시 토큰입니다.");
                return new CustomException(TokenErrorCode.EXPIRED_REFRESH_TOKEN);
            });

        Long companyId = tokenEntity.getCompanyId();
        log.info("[로그아웃 요청] 회사 ID: {}", companyId);

        if (tokenEntity.isExpired()) {
            log.warn("[로그아웃 실패] 이미 만료된 토큰입니다. 회사 ID: {}", companyId);
            return;
        }

        tokenEntity.expireToken();
        refreshTokenRepository.save(tokenEntity);
        log.info("[로그아웃 성공] 토큰 만료 처리 완료. 회사 ID: {}", companyId);
    }

    public Long extractCompanyIdFromToken(String token) {
        return jwtTokenProvider.extractCompanyId(token);
    }

}
