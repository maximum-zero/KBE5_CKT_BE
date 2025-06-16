package kernel360.ckt.admin.domain.projection;

import java.time.LocalDate;

public interface DailyVehicleLogProjection {
    LocalDate getDrivingDate();
    Long getTotalDistance();
    String getTotalDrivingTime();
}
