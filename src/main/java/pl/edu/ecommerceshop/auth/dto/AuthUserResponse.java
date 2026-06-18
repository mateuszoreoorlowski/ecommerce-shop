package pl.edu.ecommerceshop.auth.dto;

import pl.edu.ecommerceshop.auth.model.UserRole;

public record AuthUserResponse(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        UserRole role
) {
}