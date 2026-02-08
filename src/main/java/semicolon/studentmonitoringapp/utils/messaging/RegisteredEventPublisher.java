package semicolon.studentmonitoringapp.utils.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import semicolon.studentmonitoringapp.dtos.request.RegisterEventDto;

@Component
public class RegisteredEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final String EXCHANGE;
    private final String ROUTING_KEY;

    @Autowired
    public RegisteredEventPublisher(RabbitTemplate rabbitTemplate ,
                                    @Value("${app.exchange.user}") String exchange,
                                    @Value("${app.routing.user-registered}") String routingKey
                                           ){
        this.rabbitTemplate = rabbitTemplate;
        EXCHANGE = exchange;
        ROUTING_KEY = routingKey;
    }

    @Async
    public void broadcastEvent(RegisterEventDto teacher){
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, teacher);
    }
}
