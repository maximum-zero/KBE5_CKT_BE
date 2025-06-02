package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.CompanyEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CompanyMeResponse {
    private final Long id;
    private final String email;
    private final String name;
    private final String ceoName;
    private final String telNumber;
    private final LocalDateTime createAt;

    public CompanyMeResponse(CompanyEntity company) {
        this.id = company.getId();
        this.email = company.getEmail();
        this.name = company.getName();
        this.ceoName = company.getCeoName();
        this.telNumber = company.getTelNumber();
        this.createAt = company.getCreateAt();
    }
}
