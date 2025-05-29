package kernel360.ckt.auth.ui;

import kernel360.ckt.auth.application.AuthService;
import kernel360.ckt.auth.ui.dto.request.LoginRequest;
import kernel360.ckt.auth.ui.dto.request.ReissueRequest;
import kernel360.ckt.auth.ui.dto.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
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
