package kernel360.ckt.collector.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_NAME = "gps.topic.exchange";
    public static final String DB_QUEUE = "gps.db.queue";
    public static final String VIEW_QUEUE = "gps.view.queue";

    @Bean
    public TopicExchange gpsTopicExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue dbQueue() {
        return new Queue(DB_QUEUE, true);
    }

    @Bean
    public Queue viewQueue() {
        return new Queue(VIEW_QUEUE, true);
    }

    @Bean
    public Binding dbBinding() {
        return BindingBuilder.bind(dbQueue()).to(gpsTopicExchange()).with("gps.db");
    }

    @Bean
    public Binding viewBinding() {
        return BindingBuilder.bind(viewQueue()).to(gpsTopicExchange()).with("gps.view");
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}
