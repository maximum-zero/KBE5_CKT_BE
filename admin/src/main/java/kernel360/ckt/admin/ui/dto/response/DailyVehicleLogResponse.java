package kernel360.ckt.admin.ui.dto.response;

import java.sql.Date;

public class DailyVehicleLogResponse {
    private Date driveDate;
    private String dayOfWeek;
    private Long totalDistance;
    private String totalDrivingTime;

    public DailyVehicleLogResponse(
        Date driveDate,
        String dayOfWeek,
        Long totalDistance,
        String totalDrivingTime
    ) {
        this.driveDate = driveDate;
        this.dayOfWeek = dayOfWeek;
        this.totalDistance = totalDistance;
        this.totalDrivingTime = totalDrivingTime;
    }

    public Date getDriveDate() {
        return driveDate;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public Long getTotalDistance() {
        return totalDistance;
    }

    public String getTotalDrivingTime() {
        return totalDrivingTime;
    }
}
