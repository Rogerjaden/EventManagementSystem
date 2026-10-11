package com.rogerjaden.eventmanagement.service.interfaces;

import com.rogerjaden.eventmanagement.dto.request.CreateUserDto;
import com.rogerjaden.eventmanagement.dto.request.LoginUserDto;
import com.rogerjaden.eventmanagement.dto.response.UserResponse;

import java.util.Map;

public interface AuthService {
     UserResponse register(CreateUserDto createUserDto) throws RuntimeException;

     Map<String,String> login(LoginUserDto loginUserDto);
}
