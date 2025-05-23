package kernel360.ckt.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    private CompanyEntity(String email, String password, String name, String ceoName, String telNumber) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.ceoName = ceoName;
        this.telNumber = telNumber;
    }

    public static CompanyEntity create(String email, String password, String name, String ceoName, String telNumber) {
        return new CompanyEntity(email, password, name, ceoName, telNumber);
    }

    public void update(String name, String ceoName, String telNumber) {
        this.name = name;
        this.ceoName = ceoName;
        this.telNumber = telNumber;
    }
}
