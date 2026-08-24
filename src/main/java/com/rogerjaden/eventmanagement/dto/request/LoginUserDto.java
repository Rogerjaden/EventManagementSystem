package com.rogerjaden.eventmanagement.dto.request;

import lombok.Data;

@Data
public class LoginUserDto {
    private String email;
    private String password;
}
