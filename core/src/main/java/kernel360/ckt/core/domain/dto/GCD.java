package kernel360.ckt.core.domain.dto;

/**
 * GPS 상태
 */
public enum GCD {
    A("정상"),
    V("비정상"),
    O("미장착");

    private String description;

    GCD(String description) {
        this.description = description;
    }
}
