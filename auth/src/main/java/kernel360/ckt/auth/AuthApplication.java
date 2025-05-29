package kernel360.ckt.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({
    "kernel360.ckt.core.domain.entity",         // CompanyEntity core 모듈
    "kernel360.ckt.auth.infra"                  // RefreshTokenEntity 위치
})
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
