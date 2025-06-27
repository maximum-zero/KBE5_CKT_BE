package kernel360.ckt.admin.ui;

import jakarta.validation.Valid;
import kernel360.ckt.admin.application.service.DrivingLogService;
import kernel360.ckt.admin.ui.dto.request.DrivingLogListRequest;
import kernel360.ckt.admin.ui.dto.request.DrivingLogUpdateRequest;
import kernel360.ckt.admin.ui.dto.response.DrivingLogDetailResponse;
import kernel360.ckt.admin.ui.dto.response.DrivingLogListResponse;
import kernel360.ckt.admin.ui.dto.response.DrivingLogUpdateResponse;
import kernel360.ckt.core.common.response.CommonResponse;
import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/logs/drive")
public class DrivingLogController {
    private final DrivingLogService drivingLogService;

    @GetMapping
    public CommonResponse<DrivingLogListResponse> getAllDrivingLogs(
        @Valid DrivingLogListRequest request,
        @PageableDefault(size = 10) Pageable pageable
    ) {
        log.info("운행 일지 목록 조회 요청 - vehicleNumber: {}, userName: {}, startDate: {}, endDate: {}, type: {}, pageable: {}",
            request.vehicleNumber(), request.userName(), request.startDate(), request.endDate(), request.type(), pageable
        );
        DrivingLogListResponse response = drivingLogService.retrieveDrivingLogs(request.toCommand(), pageable);
        return CommonResponse.success(response);
    }

    @GetMapping("/{id}")
    public CommonResponse<DrivingLogDetailResponse> selectDrivingLog(@PathVariable Long id) {
        log.info("운행 일지 상세 정보 조회 요청, id: {}", id);
        DrivingLogDetailResponse response = drivingLogService.getDrivingLogDetail(id);
        return CommonResponse.success(response);
    }

    @PutMapping("/{id}")
    public CommonResponse<DrivingLogUpdateResponse> updateDrivingLog(
        @PathVariable Long id,
        @RequestBody DrivingLogUpdateRequest request
    ) {
        log.info("운행일지 정보 수정 요청, id: {}, 사용자 리퀘스트: {}", id, request);
        final DrivingLogEntity updated = drivingLogService.update(id, request);
        return CommonResponse.success(DrivingLogUpdateResponse.from(updated));
    }
}
