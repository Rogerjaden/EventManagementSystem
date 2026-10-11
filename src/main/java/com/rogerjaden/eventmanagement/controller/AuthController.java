package com.rogerjaden.eventmanagement.controller;


import com.rogerjaden.eventmanagement.dto.request.CreateUserDto;
import com.rogerjaden.eventmanagement.dto.request.LoginUserDto;
import com.rogerjaden.eventmanagement.security.RefreshTokenService;
import com.rogerjaden.eventmanagement.service.interfaces.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {
    private final AuthService userService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(
            AuthService userService,
            RefreshTokenService refreshTokenService) {

        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/register")
    public Object register(@Valid @RequestBody CreateUserDto createUserDto) throws RuntimeException{
        return userService.register(createUserDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@Valid @RequestBody LoginUserDto loginUserDto){
        return ResponseEntity.ok(userService.login(loginUserDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @RequestBody Map<String, String> payload) {

        String refreshToken = payload.get("refreshToken");
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Refresh token is required.");
        }
        return ResponseEntity.ok(
                refreshTokenService.refresh(refreshToken)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody Map<String, String> payload) {
        String requestToken = payload.get("refreshToken");

        if (requestToken == null || requestToken.isBlank()) {
            return ResponseEntity.badRequest().body("Refresh token is required.");
        }

        return ResponseEntity.ok(refreshTokenService.logout(requestToken));
    }
}
