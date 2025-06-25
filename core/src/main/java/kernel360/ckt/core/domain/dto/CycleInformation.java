package kernel360.ckt.core.domain.dto;

import java.time.LocalDateTime;

public record CycleInformation(
    GCD gcd,
    double lat,
    double lon,
    int ang,
    int spd,
    long sum,
    int bat,
    LocalDateTime intervalAt
) {}
