package com.example.notificationservice.consumer;

import com.example.notificationservice.model.NotificationModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class RabbitMQMessageConsumer {

    @RabbitListener(queues = {"${rabbitmq.queue}"})
    public void sendEmail(NotificationModel notification) {
       log.info(notification);
    }

}
