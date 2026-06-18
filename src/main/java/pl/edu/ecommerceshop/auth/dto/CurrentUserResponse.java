package pl.edu.ecommerceshop.auth.dto;

import pl.edu.ecommerceshop.auth.model.UserRole;

public record CurrentUserResponse(
        Long id,
        String email,
        UserRole role
) {
}