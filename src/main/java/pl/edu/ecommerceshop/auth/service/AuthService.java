package pl.edu.ecommerceshop.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.ecommerceshop.auth.dto.AuthResponse;
import pl.edu.ecommerceshop.auth.dto.AuthUserResponse;
import pl.edu.ecommerceshop.auth.dto.LoginRequest;
import pl.edu.ecommerceshop.auth.dto.RegisterRequest;
import pl.edu.ecommerceshop.auth.model.User;
import pl.edu.ecommerceshop.auth.model.UserRole;
import pl.edu.ecommerceshop.auth.repository.UserRepository;
import pl.edu.ecommerceshop.common.exception.BusinessException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String normalizedEmail = normalizeEmail(request.email());
        if (appUserRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new BusinessException("User email already exists.");
        }

        User user = new User(
                normalizedEmail,
                passwordEncoder.encode(request.password()),
                request.firstName(),
                request.lastName(),
                UserRole.CUSTOMER
        );

        User saved = appUserRepository.save(user);
        return createAuthResponse(saved);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = appUserRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password."));

        if (!user.isActive() || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email or password.");
        }

        return createAuthResponse(user);
    }

    private AuthResponse createAuthResponse(User user) {
        Jwt jwt = jwtService.generateAccessToken(user);
        return new AuthResponse(
                jwt.getTokenValue(),
                "Bearer",
                jwt.getExpiresAt(),
                new AuthUserResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getRole()
                )
        );
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }
}