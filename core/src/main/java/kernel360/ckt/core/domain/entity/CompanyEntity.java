package kernel360.ckt.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "company")
@Entity
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String email;

    @Column
    private String password;

    @Column(length = 50)
    private String name;

    @Column(length = 30)
    private String ceoName;

    @Column(length = 14)
    private String telNumber;

    @Column
    private LocalDateTime createAt;

    @Column
    private LocalDateTime updateAt;

}
