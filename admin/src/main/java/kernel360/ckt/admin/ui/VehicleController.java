package kernel360.ckt.admin.ui;

import jakarta.validation.Valid;
import kernel360.ckt.admin.application.service.VehicleService;
import kernel360.ckt.admin.application.service.command.CreateVehicleCommand;
import kernel360.ckt.admin.application.service.command.UpdateVehicleCommand;
import kernel360.ckt.admin.ui.dto.request.VehicleCreateRequest;
import kernel360.ckt.admin.ui.dto.request.VehicleKeywordRequest;
import kernel360.ckt.admin.ui.dto.request.VehicleUpdateRequest;
import kernel360.ckt.admin.ui.dto.response.*;
import kernel360.ckt.core.common.response.CommonResponse;
import kernel360.ckt.core.domain.entity.VehicleEntity;
import kernel360.ckt.core.domain.enums.VehicleStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/vehicles")
@RestController
public class VehicleController {
    private static final String X_USER_ID_HEADER = "X-User-Id";

    private final VehicleService vehicleService;

    @PostMapping
    CommonResponse<VehicleResponse> create(
        @RequestHeader(X_USER_ID_HEADER) Long companyId,
        @RequestBody @Valid VehicleCreateRequest request
    ) {
        log.info("차량 추가 요청, 사용자 리퀘스트: {}", request);
        final CreateVehicleCommand command = request.toCommand(companyId);
        final VehicleEntity vehicleEntity = vehicleService.create(command);
        return CommonResponse.success(VehicleResponse.from(vehicleEntity));
    }

    @PutMapping("/{id}")
    CommonResponse<VehicleResponse> updateVehicle(
        @RequestHeader(X_USER_ID_HEADER) Long companyId,
        @PathVariable Long id,
        @RequestBody VehicleUpdateRequest request
    ) {
        log.info("차량 정보 수정 요청, id: {}, 사용자 리퀘스트: {}", id, request);
        final UpdateVehicleCommand command = request.toCommand(companyId);
        final VehicleEntity vehicleEntity = vehicleService.update(id, command);
        return CommonResponse.success(VehicleResponse.from(vehicleEntity));
    }

    @GetMapping
    CommonResponse<VehicleListResponse> getAllVehicles(
        @RequestHeader(X_USER_ID_HEADER) Long companyId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) VehicleStatus status,
        @RequestParam(required = false) String keyword
    ) {
        log.info("차량 목록 조회 요청, page: {}, size: {}, status: {}, keyword: {}", page, size, status, keyword);
        Pageable pageable = PageRequest.of(page, size);
        Page<VehicleEntity> vehiclePage = vehicleService.searchVehicles(companyId, status, keyword, pageable);
        final VehicleListResponse vehicleResponse = VehicleListResponse.from(vehiclePage);
        return CommonResponse.success(vehicleResponse);
    }

    @GetMapping("/{id}")
    CommonResponse<VehicleResponse> selectVehicle(
        @RequestHeader(X_USER_ID_HEADER) Long companyId,
        @PathVariable Long id
    ) {
        log.info("차량 상세 정보 조회 요청, id: {}", id);
        final VehicleEntity vehicleEntity = vehicleService.findById(id, companyId);
        return CommonResponse.success(VehicleResponse.from(vehicleEntity));
    }

    @DeleteMapping("/{id}")
    CommonResponse<Void> deleteVehicle(
        @RequestHeader(X_USER_ID_HEADER) Long companyId,
        @PathVariable Long id
    ) {
        log.info("차량 삭제 요청, id: {}", id);
        vehicleService.delete(id, companyId);
        return CommonResponse.success(null);
    }

    @GetMapping("/search")
    public CommonResponse<VehicleKeywordListResponse> searchKeyword(
        @RequestHeader(X_USER_ID_HEADER) Long companyId,
        @Valid VehicleKeywordRequest request
    ) {
        final List<VehicleEntity> vehicles = vehicleService.searchKeyword(request.toCommand(companyId));
        return CommonResponse.success(VehicleKeywordListResponse.from(vehicles));
    }
}
