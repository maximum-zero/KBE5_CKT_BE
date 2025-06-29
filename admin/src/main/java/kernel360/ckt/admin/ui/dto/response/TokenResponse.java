package kernel360.ckt.admin.ui.dto.response;

public record TokenResponse(
    String accessToken,
    String refreshToken
) {}
