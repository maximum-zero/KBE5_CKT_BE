package kernel360.ckt.admin.ui.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import kernel360.ckt.admin.application.service.command.VehicleKeywordCommand;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record VehicleKeywordRequest(
    String keyword,

    @NotNull(message = "픽업 시간은 필수입니다.")
    @FutureOrPresent(message = "픽업 시간은 현재보다 미래여야 합니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime pickupAt,

    @NotNull(message = "반납 시간은 필수입니다.")
    @FutureOrPresent(message = "반납 시간은 현재보다 미래여야 합니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime returnAt
) {
    public VehicleKeywordRequest {
        if (pickupAt != null && returnAt != null && pickupAt.isAfter(returnAt)) {
            throw new IllegalArgumentException("반납 시간은 픽업 시간보다 이후여야 합니다.");
        }
    }

    public VehicleKeywordCommand toCommand(Long companyId) {
        return new VehicleKeywordCommand(
            companyId,
            this.keyword,
            this.pickupAt,
            this.returnAt
        );
    }
}
