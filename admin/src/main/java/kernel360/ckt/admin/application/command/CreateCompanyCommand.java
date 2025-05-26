package kernel360.ckt.admin.application.command;


import kernel360.ckt.admin.ui.dto.request.CompanyCreateRequest;
import kernel360.ckt.core.domain.entity.CompanyEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateCompanyCommand {
    private final String email;
    private final String password;
    private final String name;
    private final String ceoName;
    private final String telNumber;

    public static CreateCompanyCommand from(CompanyCreateRequest request) {
        return new CreateCompanyCommand(
            request.email(),
            request.password(),
            request.name(),
            request.ceoName(),
            request.telNumber()
        );
    }

    public CompanyEntity toEntity() {
        return CompanyEntity.create(this.email, this.password, this.name, this.ceoName, this.telNumber);
    }
}
