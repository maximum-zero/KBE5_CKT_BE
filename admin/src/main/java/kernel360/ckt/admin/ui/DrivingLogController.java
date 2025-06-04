package kernel360.ckt.admin.ui;

import kernel360.ckt.admin.application.DrivingLogService;
import kernel360.ckt.admin.ui.dto.response.DrivingLogListResponse;
import kernel360.ckt.core.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/logs/drive")
public class DrivingLogController {
    private final DrivingLogService drivingLogService;

    @GetMapping
    public CommonResponse<DrivingLogListResponse> getAllDrivingLogs() {
        DrivingLogListResponse response = drivingLogService.getDrivingLogList();
        return CommonResponse.success(response);
    }
}
