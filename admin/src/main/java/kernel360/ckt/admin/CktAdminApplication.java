package kernel360.ckt.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@SpringBootApplication
@EntityScan(basePackages = "kernel360.ckt.core")
@ComponentScan(basePackages = {"kernel360.ckt.core", "kernel360.ckt.admin"})
@EnableJpaRepositories(basePackages = "kernel360.ckt.admin")
public class CktAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(CktAdminApplication.class, args);
    }

}
