package kernel360.ckt.core.domain.entity;

import jakarta.persistence.*;
import kernel360.ckt.core.domain.enums.RentalStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "rental")
@Entity
public class RentalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private VehicleEntity vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentalStatus status;

    @Column
    private LocalDateTime pickupAt;

    @Column
    private LocalDateTime returnAt;

    @Column
    private LocalDateTime createAt;

    @Column
    private LocalDateTime updateAt;

    private RentalEntity(VehicleEntity vehicle, LocalDateTime pickupAt, RentalStatus status) {
        this.vehicle = vehicle;
        this.status = status;
        this.pickupAt = pickupAt;
        this.createAt = LocalDateTime.now();
    }

    public static RentalEntity create(VehicleEntity vehicle, LocalDateTime pickupAt) {
        return new RentalEntity(vehicle, pickupAt, RentalStatus.RENTED);
    }

    public void returned(LocalDateTime returnAt) {
        this.status = RentalStatus.RENTED;
        this.returnAt = returnAt;
    }

}
