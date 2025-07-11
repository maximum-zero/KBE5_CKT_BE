package kernel360.ckt.collector.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Profile("publisher")
@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean("collectorExec")
    public ThreadPoolTaskExecutor collectorExecutor() {
        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(10);
        exec.setMaxPoolSize(50);
        exec.setQueueCapacity(200);
        exec.setThreadNamePrefix("collector-");
        exec.initialize();
        return exec;
    }
}
