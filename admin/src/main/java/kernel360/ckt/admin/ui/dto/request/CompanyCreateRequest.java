package kernel360.ckt.admin.ui.dto.request;

import kernel360.ckt.core.domain.entity.CompanyEntity;

public record CompanyCreateRequest(
    String email,
    String password,
    String name,
    String ceoName,
    String telNumber
) {
    public CompanyEntity toEntity() {
        return CompanyEntity.create(email, password, name, ceoName, telNumber);
    }

}
