package com.rogerjaden.eventmanagement.mapper;

import com.rogerjaden.eventmanagement.dto.request.CreateUserDto;
import com.rogerjaden.eventmanagement.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(CreateUserDto dto) {

        User user = new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());

        return user;
    }
}
