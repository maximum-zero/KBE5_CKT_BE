package kernel360.ckt.core.domain.entity;

import jakarta.persistence.*;
import java.util.List;
import kernel360.ckt.core.domain.dto.CycleInformation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "vehicle_trace_log")
@Entity
public class VehicleTraceLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private RouteEntity route;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "trace_data_json", columnDefinition = "json")
    private List<CycleInformation> traceDataJson;

    @Column
    private LocalDateTime occurredAt;

    private VehicleTraceLogEntity(RouteEntity route, List<CycleInformation> traceDataJson, LocalDateTime occurredAt) {
        this.route = route;
        this.traceDataJson = traceDataJson;
        this.occurredAt = occurredAt;
    }

    public static VehicleTraceLogEntity create(RouteEntity route, List<CycleInformation> traceDataJson, LocalDateTime occurredAt) {
        return new VehicleTraceLogEntity(route, traceDataJson, occurredAt);
    }

}
