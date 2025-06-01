package kernel360.ckt.core.domain.entity;

import jakarta.persistence.*;
import kernel360.ckt.core.domain.enums.DrivingLogStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "driving_log")
@Entity
public class DrivingLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_id", unique = true)
    private RentalEntity rental;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private VehicleEntity vehicle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DrivingLogStatus status;

    @Lob
    private String memo;

    @Column
    private LocalDateTime createAt;

    @Column
    private LocalDateTime updateAt;

    private DrivingLogEntity(RentalEntity rental, VehicleEntity vehicle, DrivingLogStatus status) {
        this.rental = rental;
        this.vehicle = vehicle;
        this.status = status;
        this.createAt = LocalDateTime.now();
    }

    public static DrivingLogEntity create(RentalEntity rental, VehicleEntity vehicle) {
        return new DrivingLogEntity(rental, vehicle, DrivingLogStatus.STARTED);
    }

    public void inProgress() {
        this.status = DrivingLogStatus.IN_PROGRESS;
    }

    public void completed() {
        this.status = DrivingLogStatus.COMPLETED;
        this.updateAt = LocalDateTime.now();
    }
}
