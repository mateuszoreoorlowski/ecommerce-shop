package pl.edu.ecommerceshop.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.ecommerceshop.auth.dto.*;
import pl.edu.ecommerceshop.auth.model.User;
import pl.edu.ecommerceshop.auth.model.UserRole;
import pl.edu.ecommerceshop.auth.repository.UserRepository;
import pl.edu.ecommerceshop.common.exception.BusinessException;
import pl.edu.ecommerceshop.common.exception.ResourceNotFoundException;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final LoginCodeService loginCodeService;

    @Transactional
    public AuthUserResponse register(RegisterRequest request) {
        String username = normalize(request.username());
        String email = normalize(request.email());

        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new BusinessException("Username already exists.");
        }

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new BusinessException("User email already exists.");
        }

        User user = new User(
                username,
                email,
                passwordEncoder.encode(request.password()),
                request.firstName(),
                request.lastName(),
                UserRole.CUSTOMER
        );

        return mapUser(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public LoginStartResponse startLogin(LoginStartRequest request) {
        User user = findByIdentifier(request.identifier());

        if (!user.isActive() || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid login credentials.");
        }

        LoginCodeService.LoginChallenge challenge = loginCodeService.createChallenge(user);

        return new LoginStartResponse(
                challenge.challengeId(),
                challenge.maskedEmail(),
                challenge.expiresAt()
        );
    }

    @Transactional(readOnly = true)
    public AuthResponse verifyLogin(LoginVerifyRequest request) {
        Long userId = loginCodeService.verifyCode(request.challengeId(), request.code());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id %d not found.".formatted(userId)
                ));

        if (!user.isActive()) {
            throw new BadCredentialsException("Invalid login credentials.");
        }

        return createAuthResponse(user);
    }

    private User findByIdentifier(String identifier) {
        String normalized = normalize(identifier);

        return userRepository.findByEmailIgnoreCase(normalized)
                .or(() -> userRepository.findByUsernameIgnoreCase(normalized))
                .orElseThrow(() -> new BadCredentialsException("Invalid login credentials."));
    }

    private AuthResponse createAuthResponse(User user) {
        Jwt jwt = jwtService.generateAccessToken(user);

        return new AuthResponse(
                jwt.getTokenValue(),
                "Bearer",
                jwt.getExpiresAt(),
                mapUser(user)
        );
    }

    private AuthUserResponse mapUser(User user) {
        return new AuthUserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole()
        );
    }

    private String normalize(String value) {
        return value.trim().toLowerCase();
    }
}