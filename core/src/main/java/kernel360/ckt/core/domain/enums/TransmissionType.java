package kernel360.ckt.core.domain.enums;

public enum TransmissionType {
    AUTOMATIC("자동"),
    MANUAL("수동"),
    CVT("CVT"),
    DCT("DCT");

    private final String value;

    TransmissionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
