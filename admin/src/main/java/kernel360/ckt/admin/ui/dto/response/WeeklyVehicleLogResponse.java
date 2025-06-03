package kernel360.ckt.admin.ui.dto.response;

import java.sql.Date;

public class WeeklyVehicleLogResponse {
    private String weekNumber;
    private Date weekStartDate;
    private Date weekEndDate;
    private Long totalDistance;
    private String totalDrivingTime;
    private Long drivingDays;

    public WeeklyVehicleLogResponse(
        String weekNumber,
        Date weekStartDate,
        Date weekEndDate,
        Long totalDistance,
        String totalDrivingTime,
        Long drivingDays
    ) {
        this.weekNumber = weekNumber;
        this.weekStartDate = weekStartDate;
        this.weekEndDate = weekEndDate;
        this.totalDistance = totalDistance;
        this.totalDrivingTime = totalDrivingTime;
        this.drivingDays = drivingDays;
    }

    public String getWeekNumber() { return weekNumber; }
    public Date getWeekStartDate() { return weekStartDate; }
    public Date getWeekEndDate() { return weekEndDate; }
    public Long getTotalDistance() { return totalDistance; }
    public String getTotalDrivingTime() { return totalDrivingTime; }
    public Long getDrivingDays() { return drivingDays; }
}
