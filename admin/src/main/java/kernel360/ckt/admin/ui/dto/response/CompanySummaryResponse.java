package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.CompanyEntity;

public record CompanySummaryResponse(
    Long id,
    String email,
    String name,
    String ceoName,
    String telNumber
) {
    public static CompanySummaryResponse from(CompanyEntity companyEntity) {
        return new CompanySummaryResponse(
            companyEntity.getId(),
            companyEntity.getEmail(),
            companyEntity.getName(),
            companyEntity.getCeoName(),
            companyEntity.getTelNumber()
        );
    }
}
