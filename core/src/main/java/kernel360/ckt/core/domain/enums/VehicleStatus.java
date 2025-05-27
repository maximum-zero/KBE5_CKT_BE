package kernel360.ckt.core.domain.enums;

public enum VehicleStatus {
    AVAILABLE("대여 가능"),
    RENTED("대여중"),
    INACTIVE("비활성화");

    private final String description;

    VehicleStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
