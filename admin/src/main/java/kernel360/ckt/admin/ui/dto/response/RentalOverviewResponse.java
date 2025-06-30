package kernel360.ckt.admin.ui.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record RentalOverviewResponse(
    int totalCount,
    int activeCount,
    CurrentRentalResponse currentRental,
    List<RentalHistoryResponse> rentalHistory
) {}
