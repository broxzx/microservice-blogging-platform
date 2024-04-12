package com.example.notificationservice.consumer;

import com.example.notificationservice.model.NotificationModel;
import com.example.notificationservice.service.EmailSenderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class RabbitMQMessageConsumer {

    private final EmailSenderService senderService;

    public RabbitMQMessageConsumer(EmailSenderService emailSenderService, @Value("${spring.mail.activated}") boolean activated) {
        this.senderService = activated ? emailSenderService : null;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${rabbitmq.queue-subscriber}", durable = "true"),
            exchange = @Exchange(value = "${rabbitmq.exchange-subscriber}", type = ExchangeTypes.TOPIC),
            key = "${rabbitmq.routing-key-subscriber}"
    ))
    public void sendEmailToSubscriber(NotificationModel notification) {
        if (senderService != null) {
            senderService.sendEmail(
                    notification.email(),
                    "New Subscription",
                    "You subscribed to the new blog. Congratulations!");
        }
        log.info("Received notification for subscriber: {}", notification);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${rabbitmq.queue-owner}", durable = "true"),
            exchange = @Exchange(value = "${rabbitmq.exchange-owner}", type = ExchangeTypes.TOPIC),
            key = "${rabbitmq.routing-key-owner}"
    ))
    public void sendEmailToOwner(NotificationModel notification) {
        if (senderService != null) {
            senderService.sendEmail(
                    notification.email(),
                    "New Subscription on your blog",
                    "New User subscribed to your blog. Congratulations!");
        }
        log.info("Received notification for owner: {}", notification);
    }

}
