package kernel360.ckt.core.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "vehicle_trace_log")
@Entity
public class VehicleTraceLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", unique = true)
    private RouteEntity route;

    @Lob
    private String traceDataJson;

    @Column
    private LocalDateTime occurredAt;

}
