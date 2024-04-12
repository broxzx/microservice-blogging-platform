package com.example.subscriptionservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    //subscriber queue adjustment
    @Value("${rabbitmq.queue-subscriber}")
    private String queueSubscriberName;

    @Value("${rabbitmq.exchange-subscriber}")
    private String exchangeSubscriberName;

    @Value("${rabbitmq.routing-key-subscriber}")
    private String routingSubscriberKey;


    //owner queue adjustment
    @Value("${rabbitmq.queue-owner}")
    private String queueOwnerName;

    @Value("${rabbitmq.exchange-owner}")
    private String exchangeOwnerName;

    @Value("${rabbitmq.routing-key-owner}")
    private String routingOwnerKey;


    @Bean
    public Queue queueSubscriber() {
        return new Queue(queueSubscriberName);
    }

    @Bean
    public TopicExchange exchangeSubscriber() {
        return new TopicExchange(exchangeSubscriberName);
    }

    @Bean
    public Binding bindingSubscriber() {
        return BindingBuilder
                .bind(queueSubscriber())
                .to(exchangeSubscriber())
                .with(routingSubscriberKey);
    }

    @Bean
    public Queue queueOwner() {
        return new Queue(queueOwnerName);
    }

    @Bean
    public TopicExchange exchangeOwner() {
        return new TopicExchange(exchangeOwnerName);
    }

    @Bean
    public Binding bindingOwner() {
        return BindingBuilder
                .bind(queueOwner())
                .to(exchangeOwner())
                .with(routingOwnerKey);
    }


    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());

        return rabbitTemplate;
    }

}