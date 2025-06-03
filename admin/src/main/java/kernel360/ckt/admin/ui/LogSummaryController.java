package kernel360.ckt.admin.ui;

import kernel360.ckt.admin.application.LogSummaryService;
import kernel360.ckt.admin.ui.dto.response.VehicleLogSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/logs/summary")
public class LogSummaryController {

    private final LogSummaryService logSummaryService;

    @GetMapping
    public List<VehicleLogSummaryResponse> getSummary(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
        @RequestParam(required = false) String registrationNumber,
        @RequestParam(required = false) String driverName
    ) {
        return logSummaryService.getVehicleLogSummary(from, to, registrationNumber, driverName);
    }
}
