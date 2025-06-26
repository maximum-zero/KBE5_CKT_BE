package kernel360.ckt.core.domain.enums;

import lombok.Getter;

@Getter
public enum VehicleEventType {
    ON,
    OFF
    ;

    public boolean isON() {
        return this == ON;
    }

    public boolean isOFF() {
        return this == OFF;
    }
}
