package com.example.subscriptionservice.producer;

import com.example.subscriptionservice.model.NotificationModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * RabbitMQNotificationProducer is a class responsible for sending notifications
 * to subscribers and blog owners using RabbitMQ messaging.
 */
@RequiredArgsConstructor
@Service
@Log4j2
public class RabbitMQNotificationProducer {

    @Value("${rabbitmq.exchange-subscriber}")
    private String exchangeSubscriberName;

    @Value("${rabbitmq.routing-key-subscriber}")
    private String routingSubscriberKey;


    @Value("${rabbitmq.exchange-owner}")
    private String exchangeOwnerName;

    @Value("${rabbitmq.routing-key-owner}")
    private String routingOwnerKey;


    private final RabbitMessagingTemplate rabbitMessagingTemplate;

    /**
     * Sends a notification to a subscriber using RabbitMQ messaging.
     *
     * @param notificationModel The notification model to be sent.
     */
    public void sendNotificationToSubscriber(NotificationModel notificationModel) {
        rabbitMessagingTemplate.convertAndSend(exchangeSubscriberName, routingSubscriberKey, notificationModel);
        log.info("{} sent notification to the subscriber", notificationModel);
    }

    /**
     * Sends a notification to the blog owner using RabbitMQ messaging.
     * @param notificationModel The notification model to be sent.
     */
    public void sendNotificationToBlogOwner(NotificationModel notificationModel) {
        rabbitMessagingTemplate.convertAndSend(exchangeOwnerName, routingOwnerKey, notificationModel);

        log.info("{} sent notification to the owner", notificationModel);
    }


}
