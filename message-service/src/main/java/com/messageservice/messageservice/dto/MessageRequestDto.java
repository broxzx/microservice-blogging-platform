package com.messageservice.messageservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO (Data Transfer Object) class representing a message request.
 * It contains the content of the message and the ID of the blog.
 */
@Data
@NoArgsConstructor
public class MessageRequestDto {

    private String content;

    private Long blogId;
}
