package com.qf.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public FanoutExchange emailExchange(){
        return new FanoutExchange("emailExchange");
    }

    @Bean
    public Queue emailQueue(){
        return new Queue("emailQueue");
    }

    @Bean
    public Binding emailExchangeBindEmailQueue(){
        return BindingBuilder.bind(emailQueue()).to(emailExchange());
    }

}
