package kernel360.ckt.core.domain.entity;

import jakarta.persistence.*;
import kernel360.ckt.core.domain.enums.RouteStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "route")
@Entity
public class RouteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driving_log_id", unique = true)
    private DrivingLogEntity drivingLog;

    @Enumerated(EnumType.STRING)
    @Column
    private RouteStatus status;

    @Column
    private double startLat;

    @Column
    private double startLon;

    @Column
    private double endLat;

    @Column
    private double endLon;

    @Column
    private long startOdometer;

    @Column
    private long endOdometer;

    @Column
    private long totalDistance;

    @Column
    private LocalDateTime startAt;

    @Column
    private LocalDateTime endAt;

    private RouteEntity(DrivingLogEntity drivingLog, RouteStatus status, double startLat, double startLon, long startOdometer, LocalDateTime startAt) {
        this.drivingLog = drivingLog;
        this.status = status;
        this.startLat = startLat;
        this.startLon = startLon;
        this.startOdometer = startOdometer;
        this.startAt = startAt;
    }

    public static RouteEntity create(DrivingLogEntity drivingLog, double startLat, double startLon, long startOdometer, LocalDateTime startAt) {
        return new RouteEntity(drivingLog, RouteStatus.ACTIVE, startLat, startLon, startOdometer, startAt);
    }

}
