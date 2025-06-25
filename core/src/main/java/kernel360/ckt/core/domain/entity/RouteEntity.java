package kernel360.ckt.core.domain.entity;

import jakarta.persistence.*;
import kernel360.ckt.core.domain.enums.RouteStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

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
    @JoinColumn(name = "driving_log_id")
    private DrivingLogEntity drivingLog;

    @Enumerated(EnumType.STRING)
    @Column
    private RouteStatus status;

    @Column(name = "start_location", columnDefinition = "GEOMETRY(Point, 4326)")
    private Point startLocation;

    @Column(name = "end_location", columnDefinition = "GEOMETRY(Point, 4326)")
    private Point endLocation;

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
        this.startLocation = createPointFromGpsInfo(startLat, startLon);
        this.startOdometer = startOdometer;
        this.startAt = startAt;
    }

    public static RouteEntity create(DrivingLogEntity drivingLog, double startLat, double startLon, long startOdometer, LocalDateTime startAt) {
        return new RouteEntity(drivingLog, RouteStatus.ACTIVE, startLat, startLon, startOdometer, startAt);
    }

    public void completed(double endLat, double endLon, long endOdometer, LocalDateTime endAt) {
        this.endLocation = createPointFromGpsInfo(endLat, endLon);
        this.endOdometer = endOdometer;
        this.totalDistance = endOdometer - this.startOdometer;
        this.endAt = endAt;
        this.status = RouteStatus.COMPLETED;
    }

    // GPS 정보를 JTS Point 객체로 변환하는 유틸리티 메서드
    private static Point createPointFromGpsInfo(double lat, double lon) {
        final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        return geometryFactory.createPoint(new Coordinate(lon, lat));
    }

}
