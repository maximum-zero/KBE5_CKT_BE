package kernel360.ckt.admin.ui.dto.response;

import java.util.List;
import kernel360.ckt.core.domain.entity.RentalEntity;
import org.springframework.data.domain.Page;

public record RentalListResponse(
    List<RentalSummaryResponse> list,
    int page,
    int size,
    long totalElements,
    int totalPages
){
    public static RentalListResponse from(Page<RentalEntity> pageData) {
        List<RentalSummaryResponse> rentals = pageData.getContent()
            .stream()
            .map(RentalSummaryResponse::from)
            .toList();

        return new RentalListResponse(
            rentals,
            pageData.getNumber(),
            pageData.getSize(),
            pageData.getTotalElements(),
            pageData.getTotalPages()
        );
    }
}
