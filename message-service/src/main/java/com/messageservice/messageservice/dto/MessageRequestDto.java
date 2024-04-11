package com.messageservice.messageservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageRequestDto {

    private String content;

    private Long blogId;
}
