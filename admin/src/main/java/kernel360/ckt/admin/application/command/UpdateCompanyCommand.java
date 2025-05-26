package kernel360.ckt.admin.application.command;

import kernel360.ckt.admin.ui.dto.request.CompanyUpdateRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateCompanyCommand {
    private final String name;
    private final String ceoName;
    private final String telNumber;

    public static UpdateCompanyCommand from(CompanyUpdateRequest request) {
        return new UpdateCompanyCommand(request.name(), request.ceoName(), request.telNumber());
    }
}
