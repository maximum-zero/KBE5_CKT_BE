package kernel360.ckt.admin.infra.repository.projection;

public interface VehicleLogSummaryProjection {
    String getRegistrationNumber();
    String getCompanyName();
    Long getDrivingDays();
    Long getTotalDistance();
    Double getAverageDistance();
    String getAverageDrivingTime();
}
