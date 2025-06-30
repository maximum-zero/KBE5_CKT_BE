package kernel360.ckt.admin.ui.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CurrentRentalResponse(
    String vehicleName,
    String licensePlate,
    LocalDate startDate,
    LocalDate endDate,
    String status
) {}
