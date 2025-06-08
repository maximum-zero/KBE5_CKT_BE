package kernel360.ckt.core.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import kernel360.ckt.core.domain.enums.DrivingLogStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "driving_log")
@Entity
public class DrivingLogEntity extends BaseTimeEntity {

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

    private DrivingLogEntity(RentalEntity rental, VehicleEntity vehicle, DrivingLogStatus status) {
        this.rental = rental;
        this.vehicle = vehicle;
        this.status = status;
    }

    public static DrivingLogEntity create(RentalEntity rental, VehicleEntity vehicle) {
        return new DrivingLogEntity(rental, vehicle, DrivingLogStatus.STARTED);
    }

    public void inProgress() {
        this.status = DrivingLogStatus.IN_PROGRESS;
    }

    public void completed() {
        this.status = DrivingLogStatus.COMPLETED;
    }
}
