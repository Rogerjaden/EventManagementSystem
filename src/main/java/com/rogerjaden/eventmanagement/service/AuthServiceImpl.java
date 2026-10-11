package com.rogerjaden.eventmanagement.service;

import com.rogerjaden.eventmanagement.dto.response.UserResponse;
import com.rogerjaden.eventmanagement.entity.RefreshToken;
import com.rogerjaden.eventmanagement.security.JWTService;
import com.rogerjaden.eventmanagement.dto.request.CreateUserDto;
import com.rogerjaden.eventmanagement.dto.request.LoginUserDto;
import com.rogerjaden.eventmanagement.entity.enums.Role;
import com.rogerjaden.eventmanagement.entity.enums.Status;
import com.rogerjaden.eventmanagement.entity.User;
import com.rogerjaden.eventmanagement.mapper.UserMapper;
import com.rogerjaden.eventmanagement.repository.UserRepository;
import com.rogerjaden.eventmanagement.security.RefreshTokenService;
import com.rogerjaden.eventmanagement.service.interfaces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    public AuthServiceImpl(UserRepository userRepository, UserMapper userMapper, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserResponse register(CreateUserDto createUserDto) throws RuntimeException{
        if (userRepository.existsByEmail(createUserDto.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        if (userRepository.existsByPhone(createUserDto.getPhone())) {
            throw new RuntimeException("Phone already registered");
        }
        User user = userMapper.toEntity(createUserDto);
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);

        userRepository.save(user);

        return new UserResponse(user.getName(),user.getEmail(),user.getPhone(),user.getRole());
    }

    @Override
    public Map<String,String> login(LoginUserDto loginUserDto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginUserDto.getEmail(),
                        loginUserDto.getPassword()));
        if (authentication.isAuthenticated()) {
            User authenticatedUser = userRepository.findByEmail(loginUserDto.getEmail());
            if (authenticatedUser == null) {
                throw new RuntimeException("User not found");
            }
            String accessToken =
                    jwtService.generateToken(
                            authenticatedUser.getEmail(),
                            authenticatedUser.getRole()
                    );
            RefreshToken refreshToken =
                    refreshTokenService.createRefreshToken(
                            authenticatedUser.getId()
                    );
            return Map.of(
                    "accessToken", accessToken,
                    "refreshToken", refreshToken.getToken()
            );
        }
        throw new BadCredentialsException("InvalidCredentials");
    }
}
