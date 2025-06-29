package kernel360.ckt.admin.ui.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kernel360.ckt.admin.application.service.command.LoginCommand;

public record LoginRequest(
    @Size(max = 255, message = "이메일은 255자 이내여야 합니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    String email,

    @NotBlank(message = "비밀번호는 필수입니다.")
    String password
) {
    public LoginCommand toCommand() {
        return new LoginCommand(email, password);
    }
}
