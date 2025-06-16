package kernel360.ckt.admin.ui;

import jakarta.validation.Valid;
import kernel360.ckt.admin.application.service.RentalService;
import kernel360.ckt.admin.application.service.command.CreateRentalCommand;
import kernel360.ckt.admin.ui.dto.request.RentalCreateRequest;
import kernel360.ckt.admin.ui.dto.request.RentalListRequest;
import kernel360.ckt.admin.ui.dto.request.RentalStatusUpdateRequest;
import kernel360.ckt.admin.ui.dto.response.RentalCreateResponse;
import kernel360.ckt.admin.ui.dto.response.RentalListResponse;
import kernel360.ckt.admin.ui.dto.response.RentalResponse;
import kernel360.ckt.core.common.response.CommonResponse;
import kernel360.ckt.core.domain.entity.RentalEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/rentals")
@RestController
public class RentalController {
    private final RentalService rentalService;

    @PostMapping
    CommonResponse<RentalCreateResponse> createRental(
        @RequestHeader("X-User-Id") Long companyId,
        @Valid @RequestBody RentalCreateRequest request
    ) {
        final CreateRentalCommand command = request.toCommand(companyId);
        final RentalEntity rental = rentalService.createRental(command);
        return CommonResponse.success(RentalCreateResponse.from(rental));
    }

    @GetMapping
    CommonResponse<RentalListResponse> selectRentals(
        @RequestHeader("X-User-Id") Long companyId,
        @Validated RentalListRequest request,
        @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        final Page<RentalEntity> rentals = rentalService.searchRentals(
            request.toCommand(companyId),
            pageable
        );
        return CommonResponse.success(RentalListResponse.from(rentals));
    }

    @GetMapping("/{id}")
    CommonResponse<RentalResponse> selectRental(@PathVariable Long id) {
        final RentalEntity rental = rentalService.findById(id);
        return CommonResponse.success(RentalResponse.from(rental));
    }
    @PatchMapping("/{id}/status")
    public CommonResponse<RentalResponse> updateRentalStatus(
        @PathVariable Long id,
        @Valid @RequestBody RentalStatusUpdateRequest request
    ) {
        final RentalEntity updatedRental = rentalService.updateRentalStatus(id, request.status());
        return CommonResponse.success(RentalResponse.from(updatedRental));
    }

}
