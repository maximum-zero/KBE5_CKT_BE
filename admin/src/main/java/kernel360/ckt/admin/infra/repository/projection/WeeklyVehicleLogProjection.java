package kernel360.ckt.admin.infra.repository.projection;

import java.time.LocalDate;

public interface WeeklyVehicleLogProjection {
    String getWeekNumber();
    LocalDate getStartDate();
    LocalDate getEndDate();
    Double getTotalDistance();
    String getTotalDrivingTime();
    Integer getDrivingDays();
}
