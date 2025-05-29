package kernel360.ckt.core.domain.entity;

import lombok.Getter;

@Getter
public enum CustomerStatus {
    ACTIVE("회원"),
    WITHDRAWN("탈퇴"),
    DORMANT("휴면");

    private final String description;

    CustomerStatus(String description) {
        this.description = description;
    }
}
