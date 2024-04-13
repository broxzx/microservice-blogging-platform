package com.example.notificationservice.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class provides the RabbitMQ configuration for the application.
 */
@Configuration
public class RabbitMQConfiguration {

    /**
     * Returns a new instance of Jackson2JsonMessageConverter.
     *
     * @return a new instance of Jackson2JsonMessageConverter
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Returns a new instance of RabbitTemplate with the provided ConnectionFactory.
     *
     * @param connectionFactory the ConnectionFactory used for creating the RabbitTemplate
     * @return a new instance of RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());

        return rabbitTemplate;
    }

}
