package pl.edu.ecommerceshop.auth.dto;

import java.time.Instant;

public record LoginStartResponse(
        String challengeId,
        String email,
        Instant expiresAt
) {
}
