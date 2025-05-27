package kernel360.ckt.core.domain.entity;

import jakarta.persistence.*;
import kernel360.ckt.core.domain.enums.VehicleStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "vehicle")
@Entity
public class VehicleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String registrationNumber;

    @Column(length = 4)
    private String modelYear;

    @Column(length = 30)
    private String manufacturer;

    @Column(length = 30)
    private String modelName;

    @Column(length = 10)
    private String batteryVoltage;

    @Column(length = 10)
    private String fuelType;

    @Column(length = 10)
    private String transmissionType;

    @Column(length = 20)
    private String status;

    @Lob
    private String memo;

    @Column(nullable = false)
    private LocalDateTime createAt;

    @Column
    private LocalDateTime updateAt;

    private VehicleEntity(String registrationNumber, String modelYear, String manufacturer, String modelName,
                          String batteryVoltage, String fuelType, String transmissionType,
                          VehicleStatus status, String memo) {
        this.registrationNumber = registrationNumber;
        this.modelYear = modelYear;
        this.manufacturer = manufacturer;
        this.modelName = modelName;
        this.batteryVoltage = batteryVoltage;
        this.fuelType = fuelType;
        this.transmissionType = transmissionType;
        this.status = status.name();
        this.memo = memo;
        this.createAt = LocalDateTime.now();
    }

    public static VehicleEntity create(String registrationNumber, String modelYear, String manufacturer, String modelName,
                                       String batteryVoltage, String fuelType, String transmissionType,
                                       VehicleStatus status, String memo) {
        return new VehicleEntity(registrationNumber, modelYear, manufacturer, modelName,
            batteryVoltage, fuelType, transmissionType, status, memo);
    }

    // String -> Enum
    public VehicleStatus getStatusAsEnum() {
        return VehicleStatus.valueOf(this.status);
    }

    public void updateStatus(VehicleStatus status) {
        this.status = status.name();
        this.updateAt = LocalDateTime.now();
    }

    public void updateMemo(String memo) {
        this.memo = memo;
        this.updateAt = LocalDateTime.now();
    }
}
