package com.example.blogservice.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.routing-key-delete-blog}")
    private String routingKey;

    @Value("${rabbitmq.exchange-delete-blog}")
    private String exchangeName;

    /**
     * Sends a message to delete a blog with the specified id.
     * The message is sent to a RabbitMQ exchange with the given routing key.
     *
     * @param message the message containing the blog id to delete
     */
    public void sendToDeleteMessage(String message) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);

        log.info("message with blog id {} to delete was sent", message);
    }
}
