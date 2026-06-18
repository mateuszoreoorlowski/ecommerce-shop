package pl.edu.ecommerceshop.auth.dto;

import java.time.Instant;

public record AuthResponse(
        String accessToken,
        String tokenType,
        Instant expiresAt,
        AuthUserResponse user
) {
}