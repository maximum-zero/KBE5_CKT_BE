package kernel360.ckt.admin.ui;

import jakarta.validation.Valid;
import kernel360.ckt.admin.application.service.RentalService;
import kernel360.ckt.admin.application.service.command.RentalRetrieveCommand;
import kernel360.ckt.admin.ui.dto.request.RentalCreateRequest;
import kernel360.ckt.admin.ui.dto.request.RentalListRequest;
import kernel360.ckt.admin.ui.dto.request.RentalStatusUpdateRequest;
import kernel360.ckt.admin.ui.dto.response.RentalCreateResponse;
import kernel360.ckt.admin.ui.dto.response.RentalListResponse;
import kernel360.ckt.admin.ui.dto.response.RentalResponse;
import kernel360.ckt.admin.ui.dto.response.RentalStatusResponse;
import kernel360.ckt.core.common.response.CommonResponse;
import kernel360.ckt.core.domain.entity.RentalEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 예약 관리 API Controller
 *
 * 이 클래스는 클라이언트의 HTTP 요청을 받아 예약 관련 비즈니스 로직을 처리하는 진입점 역할을 합니다.
 *
 * 멀티테넌시 환경을 위해 모든 요청은 'X-User-Id' 헤더를 통해 회사 ID를 필수로 받습니다.
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/rentals")
@RestController
public class RentalController {
    private static final String X_USER_ID_HEADER = "X-User-Id";

    private final RentalService rentalService;

    /**
     * 새로운 예약을 생성 API
     *
     * @param companyId 요청 헤더에서 추출한 회사 ID
     * @param request   예약 생성에 필요한 상세 정보 {@link RentalCreateRequest} DTO
     *
     * @return 생성된 예약의 기본 정보 {@link RentalCreateResponse}
     */
    @PostMapping
    public CommonResponse<RentalCreateResponse> createRental(
        @RequestHeader(X_USER_ID_HEADER) Long companyId,
        @Valid @RequestBody RentalCreateRequest request
    ) {
        log.info("예약 생성 요청 - {}", request);
        final RentalEntity rental = rentalService.createRental(request.toCommand(companyId));
        return CommonResponse.success(RentalCreateResponse.from(rental));
    }

    /**
     * 예약 목록을 조회 API
     *
     * @param companyId 요청 헤더에서 추출한 회사 ID
     * @param request   예약 목록 조회에 필요한 검색 조건 {@link RentalListRequest} DTO
     * @param pageable  페이징 및 정렬 정보 객체
     *
     * @return 검색 조건과 페이징에 따라 조회된 예약 목록 {@link RentalListResponse}
     */
    @GetMapping
    public CommonResponse<RentalListResponse> retrieveRentals(
        @RequestHeader(X_USER_ID_HEADER) Long companyId,
        @Valid RentalListRequest request,
        @PageableDefault() Pageable pageable
    ) {
        log.info("예약 목록 요청 - {}", request);
        final Page<RentalEntity> rentals = rentalService.retrieveRentals(request.toCommand(companyId), pageable);
        return CommonResponse.success(RentalListResponse.from(rentals));
    }

    /**
     * 예약의 상세 정보를 조회 API
     *
     * @param companyId 요청 헤더에서 추출한 회사 ID
     * @param id        조회할 예약의 고유 ID
     *
     * @return 조회된 예약의 상세 정보 {@link RentalResponse}
     */
    @GetMapping("/{id}")
    public CommonResponse<RentalResponse> retrieveRental(
        @RequestHeader(X_USER_ID_HEADER) Long companyId,
        @PathVariable Long id
    ) {
        log.info("예약 상세 요청 - ID : {}, 회사 ID: {}", id, companyId);
        final RentalEntity rental = rentalService.retrieveRental(RentalRetrieveCommand.create(id, companyId));
        return CommonResponse.success(RentalResponse.from(rental));
    }

    /**
     * 예약의 상태를 변경하는 API
     *
     * @param companyId 요청 헤더에서 추출한 회사 ID
     * @param id        조회할 예약의 고유 ID
     * @param request   변경할 예약 상태 정보 {@link RentalStatusUpdateRequest} DTO
     *
     * @return 상태가 업데이트된 예약의 정보 {@link RentalStatusResponse}
     */
    @PatchMapping("/{id}/status")
    public CommonResponse<RentalStatusResponse> updateRentalStatus(
        @RequestHeader(X_USER_ID_HEADER) Long companyId,
        @PathVariable Long id,
        @Valid @RequestBody RentalStatusUpdateRequest request
    ) {
        log.info("예약 상태 변경 요청 - ID : {}, 회사 ID: {}, {}", id, companyId, request);
        final RentalEntity rental = rentalService.updateRentalStatus(request.toCommand(id, companyId));
        return CommonResponse.success(RentalStatusResponse.from(rental));
    }

}
