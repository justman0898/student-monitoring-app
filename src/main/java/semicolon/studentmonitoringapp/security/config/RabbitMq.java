package semicolon.studentmonitoringapp.security.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;


@Configuration
public class RabbitMq {
    @Value("${app.queue.user-registered}")
    private String queueName;

    @Value("${app.exchange.user}")
    private String topicExchange;

    @Value("${app.routing.user-registered}")
    private String userRegisteredRoutingKey;;

    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(topicExchange);
    }


    @Bean
    public Queue registrationQueue(){
        return new Queue(queueName, true);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Binding registrationBinding(Queue registrationQueue,
                                       TopicExchange userExchange) {

        return BindingBuilder
                .bind(registrationQueue)
                .to(userExchange)
                .with(userRegisteredRoutingKey);
    }
}
