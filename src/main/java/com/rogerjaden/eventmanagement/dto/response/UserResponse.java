package com.rogerjaden.eventmanagement.dto.response;

import com.rogerjaden.eventmanagement.entity.enums.Role;
import lombok.Data;

@Data
public class UserResponse {

    private String name;
    private String email;
    private String phone;
    private Role role;

    public UserResponse(String name, String email, String phone, Role role) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }
}
