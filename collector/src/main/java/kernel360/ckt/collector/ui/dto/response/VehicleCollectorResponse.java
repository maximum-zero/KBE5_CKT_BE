package kernel360.ckt.collector.ui.dto.response;

public record VehicleCollectorResponse(
    String mdn
) {
    public static VehicleCollectorResponse from() {
        return new VehicleCollectorResponse(null);
    }

    public static VehicleCollectorResponse from(Long vehicleId) {
        return new VehicleCollectorResponse(vehicleId.toString());
    }
}
