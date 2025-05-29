package kernel360.ckt.auth.ui.dto.response;

public record TokenResponse (
    String accessToken,
    String refreshToken
) {}
