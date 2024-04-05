package com.example.subscriptionservice.producer;

import com.example.subscriptionservice.model.NotificationModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Log4j2
public class RabbitMQNotificationProducer {

    @Value("${rabbitmq.queue}")
    private String queueName;


    @Value("${rabbitmq.exchange}")
    private String exchangeName;

    @Value("${rabbitmq.routing-key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    public void sendNotification(NotificationModel notificationModel) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, notificationModel);
        log.info(notificationModel + " send notification");
    }
}
