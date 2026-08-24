package com.rogerjaden.eventmanagement.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class CreateUserDto {
    private String name;

    private String email;

    private String password;

    private String phone;
}
