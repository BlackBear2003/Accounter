package host.luke.api.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean("api_log")
    public Queue getApiLogQueue(){
        return new Queue("api_log",true,false,false);
    }

    @Bean("api_log_exchange")
    public DirectExchange getApiLogExchange(){
        return new DirectExchange("api_log_exchange",true,false);
    }

    @Bean
    public Binding bindApiLog(){
        return BindingBuilder.bind(getApiLogQueue()).to(getApiLogExchange()).with("api.log");
    }




}
