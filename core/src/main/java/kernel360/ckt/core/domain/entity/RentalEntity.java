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

    // TODO: 차량 엔티티로 변경 필요- 현경님 PR
    @Column(nullable = false)
    private Long vehicleId;

    // TODO: 고객 엔티티로 변경 필요 - 하경님 PR
    @Column(nullable = false)
    private Long customerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentalStatus status;

    // TODO: 감사 listener 로 변경 - 하경님 PR
    @Column
    private LocalDateTime createAt;

    @Column
    private LocalDateTime updateAt;

    private RentalEntity(Long vehicleId, RentalStatus status) {
        this.vehicleId = vehicleId;
        this.status = status;
    }

    public static RentalEntity create(Long vehicleId) {
        return new RentalEntity(vehicleId, RentalStatus.RENTED);
    }

}
