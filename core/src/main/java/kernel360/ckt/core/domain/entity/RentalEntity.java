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
public class RentalEntity extends BaseTimeEntity {

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
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentalStatus status;

    @Column
    private LocalDateTime pickupAt;

    @Column
    private LocalDateTime returnAt;

    @Lob
    private String memo;

    private RentalEntity(CompanyEntity company, VehicleEntity vehicle, CustomerEntity customer, LocalDateTime pickupAt, LocalDateTime returnAt, RentalStatus status, String memo) {
        this.company = company;
        this.vehicle = vehicle;
        this.customer = customer;
        this.status = status;
        this.pickupAt = pickupAt;
        this.returnAt = returnAt;
        this.memo = memo;
    }

    public static RentalEntity create(CompanyEntity company, VehicleEntity vehicle, CustomerEntity customer, LocalDateTime pickupAt, LocalDateTime returnAt, String memo) {
        return new RentalEntity(company, vehicle, customer, pickupAt, returnAt, RentalStatus.PENDING, memo);
    }

    public void changeStatus(RentalStatus status) {
        this.status.update(this, status);
    }

    public void updateStatus(RentalStatus status) {
        this.status = status;
    }

}
