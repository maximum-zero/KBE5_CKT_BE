package kernel360.ckt.admin.infra.repository.projection;

import java.time.LocalDate;

public interface DailyVehicleLogProjection {
    LocalDate getDrivingDate();
    Long getTotalDistance();
    String getTotalDrivingTime();
}
