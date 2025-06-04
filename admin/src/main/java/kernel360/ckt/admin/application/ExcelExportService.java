package kernel360.ckt.admin.application;

import kernel360.ckt.admin.ui.dto.response.DailyVehicleLogResponse;
import kernel360.ckt.admin.infra.repository.jpa.RouteJpaRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelExportService {

    private final RouteJpaRepository routeJpaRepository;

    /**
     * “업무용승용차 운행기록부” 양식에 맞춘 엑셀을 생성하여 byte[]로 반환합니다.
     * - from/to, registrationNumber를 받아 SQL 조회 → 일별 통계
     * - 양식 상단 제목+기본정보 영역 삽입
     * - 일별 통계 테이블(사용일자·요일, 총 주행거리, 총 운행시간) 삽입
     * - 마지막에 합계 행(총 합산) 삽입
     */
    public byte[] createExcel(
        LocalDateTime from,
        LocalDateTime to,
        String registrationNumber,
        String driverName
    ) {
        // 1) 먼저 일별 통계 데이터를 조회
        List<DailyVehicleLogResponse> dailyList =
            routeJpaRepository.findDailyVehicleLogSummary(from, to, registrationNumber);

        // 2) 엑셀 워크북/시트 생성
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("운행 기록부");

            // ==== 폰트/스타일 설정 ======================================================

            // (가) 제목용 굵은 큰 글씨
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 16);

            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);

            // (나) 소제목(“1. 기본정보”, “2. 일별 운행 내역”) 굵게
            Font subtitleFont = workbook.createFont();
            subtitleFont.setBold(true);

            CellStyle subtitleStyle = workbook.createCellStyle();
            subtitleStyle.setFont(subtitleFont);

            // (다) 테이블 헤더용 배경/굵은 선
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // (라) 일반 셀(표 내용)
            CellStyle bodyStyle = workbook.createCellStyle();
            bodyStyle.setBorderBottom(BorderStyle.THIN);
            bodyStyle.setBorderTop(BorderStyle.THIN);
            bodyStyle.setBorderLeft(BorderStyle.THIN);
            bodyStyle.setBorderRight(BorderStyle.THIN);
            bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // (마) 우측 정렬: 숫자나 시간 찍을 때
            CellStyle rightStyle = workbook.createCellStyle();
            rightStyle.cloneStyleFrom(bodyStyle);
            rightStyle.setAlignment(HorizontalAlignment.RIGHT);

            // (바) 합계 행: 굵은 테두리 + 숫자 우측정렬
            CellStyle sumStyle = workbook.createCellStyle();
            sumStyle.cloneStyleFrom(rightStyle);
            sumStyle.setFont(subtitleFont);

            // ==== 컬럼 너비 설정 ===========================================================
            sheet.setColumnWidth(0, 15 * 256); // A: 사용일자(요일)
            sheet.setColumnWidth(1, 15 * 256); // B: 총 주행거리(km)
            sheet.setColumnWidth(2, 15 * 256); // C: 총 운행시간

            int rowIdx = 0;

            // === (1) 상단 제목: "업무용승용차 운행기록부" ===
            Row titleRow = sheet.createRow(rowIdx++);
            titleRow.setHeightInPoints(30);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("업무용승용차 운행기록부");
            titleCell.setCellStyle(titleStyle);
            // A1 ~ C1 병합
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 2));

            rowIdx++; // 한 줄 띄우기

            // === (2) 기본정보 섹션 ===
            Row basicInfoSubtitle = sheet.createRow(rowIdx++);
            Cell basicInfoCell = basicInfoSubtitle.createCell(0);
            basicInfoCell.setCellValue("1. 기본정보");
            basicInfoCell.setCellStyle(subtitleStyle);

            // (2-1) “①차종” 라벨 + 빈칸
            Row vehicleInfoRow = sheet.createRow(rowIdx++);
            Cell vehicleLabelCell = vehicleInfoRow.createCell(0);
            vehicleLabelCell.setCellValue("①차종:");
            vehicleLabelCell.setCellStyle(bodyStyle);
            Cell vehicleValueCell = vehicleInfoRow.createCell(1);
            vehicleValueCell.setCellValue("");
            vehicleValueCell.setCellStyle(bodyStyle);

            // (2-2) “②자동차등록번호” 라벨 + 빈칸
            Row regnumInfoRow = sheet.createRow(rowIdx++);
            Cell regnumLabelCell = regnumInfoRow.createCell(0);
            regnumLabelCell.setCellValue("②자동차등록번호:");
            regnumLabelCell.setCellStyle(bodyStyle);
            Cell regnumValueCell = regnumInfoRow.createCell(1);
            regnumValueCell.setCellValue(registrationNumber);
            regnumValueCell.setCellStyle(bodyStyle);

            rowIdx++; // 한 줄 띄우기

            // === (3) 일별 운행 내역 섹션 제목 ===
            Row dailySubtitle = sheet.createRow(rowIdx++);
            Cell dailyTitleCell = dailySubtitle.createCell(0);
            dailyTitleCell.setCellValue("2. 일별 운행 내역");
            dailyTitleCell.setCellStyle(subtitleStyle);

            // === (4) 테이블 헤더 ===
            Row headerRow = sheet.createRow(rowIdx++);
            String[] headers = { "사용일자(요일)", "총 주행거리(km)", "총 운행시간" };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // === (5) 데이터 행 ===
            long sumDistance = 0;
            long totalSeconds = 0;

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (DailyVehicleLogResponse dto : dailyList) {
                Row row = sheet.createRow(rowIdx++);

                // A열: 날짜(요일)
                Cell cellA = row.createCell(0);
                String displayDate = dto.getDriveDate().toLocalDate().format(dateFormatter) + " (" + dto.getDayOfWeek() + ")";

                cellA.setCellValue(displayDate);
                cellA.setCellStyle(bodyStyle);

                // B열: 총 주행거리
                Cell cellB = row.createCell(1);
                long dist = dto.getTotalDistance() == null ? 0L : dto.getTotalDistance();
                cellB.setCellValue(dist);
                cellB.setCellStyle(rightStyle);
                sumDistance += dist;

                // C열: 총 운행시간 (HH:mm:ss 형식)
                Cell cellC = row.createCell(2);
                String hhmmss = dto.getTotalDrivingTime() == null ? "00:00:00" : dto.getTotalDrivingTime();
                cellC.setCellValue(hhmmss);
                cellC.setCellStyle(rightStyle);

                // “HH:mm:ss” 문자열을 초 단위로 변환하여 누적
                String[] parts = hhmmss.split(":");
                if (parts.length == 3) {
                    long h = Long.parseLong(parts[0]);
                    long m = Long.parseLong(parts[1]);
                    long s = Long.parseLong(parts[2]);
                    totalSeconds += (h * 3600 + m * 60 + s);
                }
            }

            // === (6) 합계 행 ===
            Row sumRow = sheet.createRow(rowIdx++);
            Cell sumLabel = sumRow.createCell(0);
            sumLabel.setCellValue("합계");
            sumLabel.setCellStyle(subtitleStyle);

            // 합산된 총 주행거리
            Cell sumDistCell = sumRow.createCell(1);
            sumDistCell.setCellValue(sumDistance);
            sumDistCell.setCellStyle(sumStyle);

            // 합산된 “총 운행시간” (초 → HH:mm:ss 변환)
            long seconds = totalSeconds;
            long hh = seconds / 3600;
            long mm = (seconds % 3600) / 60;
            long ss = seconds % 60;
            String totalHHMMSS = String.format("%02d:%02d:%02d", hh, mm, ss);

            Cell sumTimeCell = sumRow.createCell(2);
            sumTimeCell.setCellValue(totalHHMMSS);
            sumTimeCell.setCellStyle(sumStyle);

            // ==== 출력 완료 ====
            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("엑셀 생성 중 오류 발생", e);
        }
    }
}
