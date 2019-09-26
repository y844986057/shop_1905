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
    public Queue getSearchQueue(){
        return new Queue("search_queue");
    }

    @Bean
    public Queue getItemQueue(){
        return new Queue("item_queue");
    }

    @Bean
    public FanoutExchange getFanoutExchange(){
        return new FanoutExchange("goods_exchange");
    }

    @Bean
    public Binding getBinding1(){
        return BindingBuilder.bind(getSearchQueue()).to(getFanoutExchange());
    }

    @Bean
    public Binding getBinding2(){
        return BindingBuilder.bind(getItemQueue()).to(getFanoutExchange());
    }


}
