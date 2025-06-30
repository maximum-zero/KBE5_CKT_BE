package kernel360.ckt.admin.domain.projection;

public interface RunningVehicleProjection {
    Long getVehicleId();
    String getRegistrationNumber();
    String getManufacturer();
    String getModelName();
    String getCustomerName();
    String getLat();
    String getLon();
    String getSpd();
}
