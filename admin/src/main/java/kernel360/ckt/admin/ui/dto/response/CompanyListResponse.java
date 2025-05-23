package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.core.domain.entity.CompanyEntity;

import java.util.List;

public record CompanyListResponse(
    List<CompanySummaryResponse> list
) {
    public static CompanyListResponse from(List<CompanyEntity> companyEntityList) {
        return new CompanyListResponse(
            companyEntityList
                .stream()
                .map(CompanySummaryResponse::from)
                .toList()
        );
    }
}
