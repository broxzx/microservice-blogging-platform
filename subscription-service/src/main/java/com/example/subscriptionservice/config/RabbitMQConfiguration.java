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

/**
 * Configuration class for RabbitMQ.
 */
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


    /**
     * Creates a subscriber queue for RabbitMQ.
     *
     * @return The subscriber queue.
     */
    @Bean
    public Queue queueSubscriber() {
        return new Queue(queueSubscriberName);
    }

    /**
     * Creates a TopicExchange for RabbitMQ.
     *
     * @return The TopicExchange.
     */
    @Bean
    public TopicExchange exchangeSubscriber() {
        return new TopicExchange(exchangeSubscriberName);
    }

    /**
     * Creates a binding for the subscriber queue to the subscriber exchange with the specified routing key.
     *
     * @return The Binding object representing the binding relationship between the subscriber queue and the subscriber exchange.
     */
    @Bean
    public Binding bindingSubscriber() {
        return BindingBuilder
                .bind(queueSubscriber())
                .to(exchangeSubscriber())
                .with(routingSubscriberKey);
    }

    /**
     * Creates and returns a Queue object for the owner in RabbitMQ.
     *
     * @return The Queue object for the owner.
     *
     * @see RabbitMQConfiguration#queueOwnerName
     */
    @Bean
    public Queue queueOwner() {
        return new Queue(queueOwnerName);
    }

    /**
     * Creates and returns a TopicExchange for the owner in RabbitMQ.
     *
     * @return The TopicExchange object for the owner.
     *
     * @see RabbitMQConfiguration#exchangeOwnerName
     */
    @Bean
    public TopicExchange exchangeOwner() {
        return new TopicExchange(exchangeOwnerName);
    }

    /**
     * Creates a binding for the owner queue to the owner exchange with the specified routing key.
     *
     * @return The Binding object representing the binding relationship between the owner queue and the owner exchange.
     */
    @Bean
    public Binding bindingOwner() {
        return BindingBuilder
                .bind(queueOwner())
                .to(exchangeOwner())
                .with(routingOwnerKey);
    }


    /**
     * Creates and returns a MessageConverter object for RabbitMQ.
     *
     * @return The MessageConverter object for RabbitMQ.
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Creates a RabbitTemplate for RabbitMQ messaging.
     *
     * @param connectionFactory The ConnectionFactory used to create the RabbitTemplate.
     * @return The RabbitTemplate object.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());

        return rabbitTemplate;
    }

}