package kernel360.ckt.admin.ui;

import jakarta.validation.Valid;
import kernel360.ckt.admin.application.service.AuthService;
import kernel360.ckt.admin.ui.dto.request.LoginRequest;
import kernel360.ckt.admin.ui.dto.request.ReissueRequest;
import kernel360.ckt.admin.ui.dto.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request.toCommand()));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(@RequestBody ReissueRequest request) {
        return ResponseEntity.ok(authService.reissue(request.toCommand()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String bearer) {
        authService.logout(bearer);
        return ResponseEntity.noContent().build();
    }
}
