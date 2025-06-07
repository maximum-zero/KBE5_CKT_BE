package kernel360.ckt.admin.ui;

import kernel360.ckt.admin.application.DrivingLogService;
import kernel360.ckt.admin.ui.dto.request.DrivingLogUpdateRequest;
import kernel360.ckt.admin.ui.dto.response.DrivingLogDetailResponse;
import kernel360.ckt.admin.ui.dto.response.DrivingLogListResponse;
import kernel360.ckt.admin.ui.dto.response.DrivingLogUpdateResponse;
import kernel360.ckt.admin.ui.dto.response.VehicleResponse;
import kernel360.ckt.core.common.response.CommonResponse;
import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.VehicleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/logs/drive")
public class DrivingLogController {
    private final DrivingLogService drivingLogService;

    @GetMapping
    public CommonResponse<DrivingLogListResponse> getAllDrivingLogs(
        @RequestParam(required = false) String vehicleNumber,
        @RequestParam(required = false) String userName,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @PageableDefault(size = 10, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        DrivingLogListResponse response = drivingLogService.getDrivingLogList(vehicleNumber, userName, startDate, endDate,pageable);
        return CommonResponse.success(response);
    }

    @GetMapping("/{id}")
    public CommonResponse<DrivingLogDetailResponse> selectDrivingLog(@PathVariable Long id) {
        DrivingLogDetailResponse response = drivingLogService.getDrivingLogDetail(id);
        return CommonResponse.success(response);
    }

    @PutMapping("/{id}")
    public CommonResponse<DrivingLogUpdateResponse> updateDrivingLog(
        @PathVariable Long id,
        @RequestBody DrivingLogUpdateRequest request
    ) {
        final DrivingLogEntity updated = drivingLogService.update(id, request);
        return CommonResponse.success(DrivingLogUpdateResponse.from(updated));
    }
}
