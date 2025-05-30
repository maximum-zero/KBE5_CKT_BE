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

    private Long vehicleId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DrivingLogStatus status;

    @Lob
    private String memo;

    // TODO: 감사 listener 로 변경 필요- 하경님 PR
    @Column
    private LocalDateTime createAt;

    @Column
    private LocalDateTime updateAt;

    private DrivingLogEntity(RentalEntity rental, DrivingLogStatus status) {
        this.rental = rental;
        this.status = status;
    }

    public static DrivingLogEntity create(RentalEntity rental) {
        return new DrivingLogEntity(rental, DrivingLogStatus.STARTED);
    }
}
