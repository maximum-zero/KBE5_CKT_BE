package kernel360.ckt.admin.dto.response;

import kernel360.ckt.core.domain.entity.CompanyEntity;

public record CompanyCreateResponse(
    String email,
    String name,
    String ceoName,
    String telNumber
) {
    public static CompanyCreateResponse from(CompanyEntity companyEntity) {
        return new CompanyCreateResponse(
            companyEntity.getEmail(),
            companyEntity.getName(),
            companyEntity.getCeoName(),
            companyEntity.getTelNumber()
        );
    }
}
