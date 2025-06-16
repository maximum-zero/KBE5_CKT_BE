package kernel360.ckt.core.domain.enums;

public enum VehicleStatus {
    AVAILABLE("대여 가능"),
    RENTED("대여중"),
    INACTIVE("비활성화");

    private final String value;

    VehicleStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
