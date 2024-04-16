package com.example.security.dto;

import lombok.Builder;
import lombok.Data;

/**
 * The UserDtoResponse class represents the response data transfer object for a user.
 */
@Data
@Builder
public class UserDtoResponse {

    private String userId;

    private String username;

    private String email;

}
