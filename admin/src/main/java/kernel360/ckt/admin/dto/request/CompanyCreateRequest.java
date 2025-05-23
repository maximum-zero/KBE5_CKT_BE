package kernel360.ckt.admin.dto.request;

import kernel360.ckt.core.domain.entity.CompanyEntity;

public record CompanyCreateRequest(
    String email,
    String password,
    String name,
    String ceoName,
    String telNumber
) {
    public CompanyEntity toEntity() {
        return CompanyEntity.builder()
            .email(email)
            .password(password)
            .name(name)
            .ceoName(ceoName)
            .telNumber(telNumber)
            .build();
    }
}
