package com.rogerjaden.eventmanagement.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class CreateUserDto {
    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;

    @NotBlank
    private String phone;
}
