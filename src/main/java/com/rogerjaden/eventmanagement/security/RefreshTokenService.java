package com.rogerjaden.eventmanagement.security;

import com.rogerjaden.eventmanagement.entity.RefreshToken;
import com.rogerjaden.eventmanagement.entity.User;
import com.rogerjaden.eventmanagement.repository.RefreshTokenRepository;
import com.rogerjaden.eventmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${jwt.refreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JWTService jwtService;

    public RefreshTokenService(RefreshTokenRepository repo, UserRepository userRepository, JWTService jwtService) {
        this.refreshTokenRepository = repo;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public RefreshToken createRefreshToken(Long userId) {
        var token = new RefreshToken();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        token.setUser(user);
        token.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        token.setToken(UUID.randomUUID().toString());
        return refreshTokenRepository.save(token);
    }
    public Map<String, String> refresh(String requestToken) {

        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(requestToken)
                .orElseThrow(() ->
                        new RuntimeException("Invalid refresh token"));

        if (isTokenExpired(refreshToken)) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired");
        }

        User user = refreshToken.getUser();

        String accessToken =
                jwtService.generateToken(user.getEmail(),user.getRole());

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken.getToken()
        );
    }


    public boolean isTokenExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }

    public Object logout(String requestToken) {
        return refreshTokenRepository.findByToken(requestToken)
                .map(token -> {
                    refreshTokenRepository.delete(token);
                    return ResponseEntity.ok("Logged out successfully.");
                })
                .orElse(ResponseEntity.badRequest().body("Invalid refresh token."));
    }
}
