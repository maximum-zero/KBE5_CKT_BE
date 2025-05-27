package kernel360.ckt.emulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = "kernel360.ckt.core")
@ComponentScan(basePackages = {"kernel360.ckt.core"})
public class EmulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmulatorApplication.class, args);
    }

}
