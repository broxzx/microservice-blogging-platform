package com.messageservice.messageservice.listener;

import com.messageservice.messageservice.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class RabbitMQBlogListener {

    private final MessageService messageService;

    /**
     * This method is invoked when a message is received from RabbitMQ.
     * It deletes the message entity associated with a specific blog identified by its blogId.
     *
     * @param blogId The ID of the blog.
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("${rabbitmq.queue-delete-blog}"),
            exchange = @Exchange("${rabbitmq.exchange-delete-blog}"),
            key = "${rabbitmq.routing-key-delete-blog}"
    ))
    public void onMessage(String blogId) {
        messageService.deleteMessageEntityByBlogId(blogId);
    }
}
