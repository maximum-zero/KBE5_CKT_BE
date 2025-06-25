package kernel360.ckt.core.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

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

    @Column(name = "location", columnDefinition = "GEOMETRY(Point, 4326)")
    private Point location;

    @Column(name = "angle")
    private Integer angle;

    @Column(name = "speed")
    private Integer speed;

    @Column(name = "total_distance")
    private Long totalDistance;

    @Column(name = "battery_voltage")
    private Integer batteryVoltage;

    @Column
    private LocalDateTime occurredAt;

    // 생성자 수정
    private VehicleTraceLogEntity(
        RouteEntity route,
        Point location,
        Integer angle,
        Integer speed,
        Long totalDistance,
        Integer batteryVoltage,
        LocalDateTime occurredAt
    ) {
        this.route = route;
        this.location = location;
        this.angle = angle;
        this.speed = speed;
        this.totalDistance = totalDistance;
        this.batteryVoltage = batteryVoltage;
        this.occurredAt = occurredAt;
    }

    public static VehicleTraceLogEntity create(
        RouteEntity route,
        Double lat,
        Double lon,
        Integer angle,
        Integer speed,
        Long totalDistance,
        Integer batteryVoltage,
        LocalDateTime occurredAt
    ) {
        final Point location = createPointFromGpsInfo(lat, lon);
        return new VehicleTraceLogEntity(
            route, location, angle, speed, totalDistance, batteryVoltage, occurredAt
        );
    }

    // GPS 정보를 JTS Point 객체로 변환하는 유틸리티 메서드
    private static Point createPointFromGpsInfo(double lat, double lon) {
        final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        return geometryFactory.createPoint(new Coordinate(lon, lat));
    }
}
