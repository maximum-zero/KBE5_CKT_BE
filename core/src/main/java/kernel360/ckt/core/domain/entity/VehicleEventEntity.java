package kernel360.ckt.core.domain.entity;

import jakarta.persistence.*;
import kernel360.ckt.core.domain.enums.VehicleEventType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "vehicle_event")
@Entity
public class VehicleEventEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long vehicleId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleEventType type;

    @Column
    private Long rentalId;

    private VehicleEventEntity(Long vehicleId, VehicleEventType type, Long rentalId) {
        this.vehicleId = vehicleId;
        this.type = type;
        this.rentalId = rentalId;
    }

    public static VehicleEventEntity create(Long vehicleId, VehicleEventType type, Long rentalId) {
        return new VehicleEventEntity(vehicleId, type, rentalId);
    }

}
