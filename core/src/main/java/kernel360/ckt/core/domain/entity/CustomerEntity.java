package kernel360.ckt.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerType;

    @Column(nullable = false)
    private String phoneNumber;

    private String licenseNumber;
    private String zipCode;
    private String address;
    private String detailedAddress;
    private String birthday;
    private String memo;

    @Column(nullable = false)
    private String status;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
