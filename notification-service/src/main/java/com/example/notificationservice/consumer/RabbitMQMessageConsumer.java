package com.example.notificationservice.consumer;

import com.example.notificationservice.model.NotificationModel;
import com.example.notificationservice.service.EmailSenderService;
import lombok.extern.log4j.Log4j2;
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

    @RabbitListener(queues = {"${rabbitmq.queue}"})
    public void sendEmail(NotificationModel notification) {

        if (senderService != null) {
            senderService.sendEmail(
                    notification.email(),
                    "New Subscription",
                    "You subscribed to the new blog. Congratulations!");
        }

        log.info(notification);
    }

}
