package pl.edu.ecommerceshop.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pl.edu.ecommerceshop.auth.dto.AuthResponse;
import pl.edu.ecommerceshop.auth.dto.CurrentUserResponse;
import pl.edu.ecommerceshop.auth.dto.LoginRequest;
import pl.edu.ecommerceshop.auth.dto.RegisterRequest;
import pl.edu.ecommerceshop.auth.service.AuthService;
import pl.edu.ecommerceshop.auth.model.UserRole;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public CurrentUserResponse me(@AuthenticationPrincipal Jwt jwt) {
        Number userId = jwt.getClaim("userId");
        return new CurrentUserResponse(
                userId.longValue(),
                jwt.getClaimAsString("email"),
                UserRole.valueOf(jwt.getClaimAsString("role"))
        );
    }
}