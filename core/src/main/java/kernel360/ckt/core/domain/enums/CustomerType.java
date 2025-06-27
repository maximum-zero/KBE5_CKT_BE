package kernel360.ckt.core.domain.enums;

import lombok.Getter;

@Getter
public enum CustomerType {
    INDIVIDUAL("개인"),
    CORPORATE("법인");

    private final String value;

    CustomerType(String value) {
        this.value = value;
    }
}
