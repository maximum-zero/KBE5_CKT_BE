package kernel360.ckt.admin.application.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateCompanyCommand {
    private final String name;
    private final String ceoName;
    private final String telNumber;
}
