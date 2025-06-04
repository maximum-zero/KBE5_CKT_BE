package kernel360.ckt.admin.ui;

import jakarta.servlet.http.HttpServletResponse;
import kernel360.ckt.admin.application.ExcelExportService;
import kernel360.ckt.admin.application.LogSummaryService;
import kernel360.ckt.admin.ui.dto.response.DailyVehicleLogResponse;
import kernel360.ckt.admin.ui.dto.response.VehicleLogSummaryResponse;
import kernel360.ckt.admin.ui.dto.response.WeeklyVehicleLogResponse;
import kernel360.ckt.core.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/logs/summary")
public class LogSummaryController {

    private final LogSummaryService logSummaryService;
    private final ExcelExportService excelExportService;

    @GetMapping
    public List<VehicleLogSummaryResponse> getSummary(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
        @RequestParam(required = false) String registrationNumber,
        @RequestParam(required = false) String driverName
    ) {
        return logSummaryService.getVehicleLogSummary(from, to, registrationNumber, driverName);
    }

    @GetMapping("/weekly")
    public List<WeeklyVehicleLogResponse> getWeeklySummary(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
        @RequestParam String registrationNumber
    ) {
        return logSummaryService.getWeeklyVehicleLogSummary(from, to, registrationNumber);
    }

    @GetMapping("/daily")
    public CommonResponse<List<DailyVehicleLogResponse>> getDailySummary(
        @RequestParam
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart,
        @RequestParam
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekEnd,
        @RequestParam String registrationNumber
    ) {
        var dailyList = logSummaryService.getDailyVehicleLogSummary(weekStart, weekEnd, registrationNumber);
        return CommonResponse.success(dailyList);
    }

    @GetMapping("/excel")
    public void downloadSummaryExcel(
        @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
        @RequestParam("to")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
        @RequestParam("registrationNumber") String registrationNumber,
        HttpServletResponse response
    ) throws IOException {

        byte[] excelBytes = excelExportService.createExcel(from, to, registrationNumber, null);

        // 파일명: “운행기록부_yyyyMMdd_HHmmss.xlsx”
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = "운행기록부_" + timestamp + ".xlsx";

        // 한글 인코딩 처리
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename*=UTF-8''" + encodedFileName);

        response.getOutputStream().write(excelBytes);
        response.getOutputStream().flush();
    }

}
