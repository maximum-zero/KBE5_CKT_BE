package kernel360.ckt.core.domain.enums;

public enum FuelType {
    GASOLINE("가솔린"),
    DIESEL("디젤"),
    ELECTRIC("전기"),
    HYBRID("하이브리드"),
    LPG("LPG");

    private final String value;

    FuelType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
