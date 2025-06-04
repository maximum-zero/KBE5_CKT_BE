package kernel360.ckt.admin.ui;

import kernel360.ckt.admin.application.DrivingLogService;
import kernel360.ckt.admin.ui.dto.response.DrivingLogListResponse;
import kernel360.ckt.core.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/logs/drive")
public class DrivingLogController {
    private final DrivingLogService drivingLogService;

    @GetMapping
    public CommonResponse<DrivingLogListResponse> getAllDrivingLogs(
        @RequestParam(required = false) String vehicleNumber,
        @RequestParam(required = false) String userName,
        @PageableDefault(size = 10, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        DrivingLogListResponse response = drivingLogService.getDrivingLogList(vehicleNumber, userName, pageable);
        return CommonResponse.success(response);
    }
}
