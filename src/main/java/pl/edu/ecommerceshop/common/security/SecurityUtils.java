package pl.edu.ecommerceshop.common.security;

import org.springframework.security.oauth2.jwt.Jwt;
import pl.edu.ecommerceshop.common.exception.BusinessException;

import java.util.List;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static String currentUserEmail(Jwt jwt) {
        if (jwt == null) {
            throw new BusinessException("Authenticated user is required.");
        }

        String email = jwt.getClaimAsString("email");
        if (email == null || email.isBlank()) {
            email = jwt.getSubject();
        }

        if (email == null || email.isBlank()) {
            throw new BusinessException("Authenticated user email is missing.");
        }

        return email.trim().toLowerCase();
    }

    public static boolean isAdmin(Jwt jwt) {
        if (jwt == null) {
            return false;
        }

        List<String> roles = jwt.getClaimAsStringList("roles");
        if (roles != null && roles.contains("ROLE_ADMIN")) {
            return true;
        }

        return "ADMIN".equals(jwt.getClaimAsString("role"));
    }
}