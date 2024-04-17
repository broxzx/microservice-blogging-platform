package com.example.blogservice.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    @Value("${rabbitmq.queue-delete-blog}")
    private String queueName;

    @Value("${rabbitmq.routing-key-delete-blog}")
    private String routingKey;

    @Value("${rabbitmq.exchange-delete-blog}")
    private String exchangeName;

    @Bean
    public Queue blogDeleteQueue() {
        return new Queue(queueName);
    }

    @Bean
    public TopicExchange blogDeleteExchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Binding blogDeleteBinding(Queue blogDeleteQueue, TopicExchange blogDeleteExchange) {
        return BindingBuilder
                .bind(blogDeleteQueue)
                .to(blogDeleteExchange)
                .with(routingKey);
    }
}
