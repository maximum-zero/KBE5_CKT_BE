package kernel360.ckt.admin.ui;

import kernel360.ckt.admin.application.service.DrivingLogService;
import kernel360.ckt.admin.ui.dto.request.DrivingLogUpdateRequest;
import kernel360.ckt.admin.ui.dto.response.DrivingLogDetailResponse;
import kernel360.ckt.admin.ui.dto.response.DrivingLogListResponse;
import kernel360.ckt.admin.ui.dto.response.DrivingLogUpdateResponse;
import kernel360.ckt.core.common.response.CommonResponse;
import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.enums.DrivingType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/logs/drive")
public class DrivingLogController {
    private final DrivingLogService drivingLogService;

    @GetMapping
    public CommonResponse<DrivingLogListResponse> getAllDrivingLogs(
        @RequestParam(required = false) String vehicleNumber,
        @RequestParam(required = false) String userName,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
        @RequestParam(required = false) DrivingType type,
        @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        log.info("운행 일지 목록 조회 요청 - vehicleNumber: {}, userName: {}, startDate: {}, endDate: {}, type: {}, pageable: {}",
            vehicleNumber,
            userName,
            startDate,
            endDate,
            type,
            pageable
        );
        DrivingLogListResponse response = drivingLogService.getDrivingLogList(vehicleNumber, userName, startDate, endDate, type, pageable);
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
