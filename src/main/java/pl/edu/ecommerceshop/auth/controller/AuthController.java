package pl.edu.ecommerceshop.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pl.edu.ecommerceshop.auth.dto.*;
import pl.edu.ecommerceshop.auth.model.UserRole;
import pl.edu.ecommerceshop.auth.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthUserResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login/start")
    public LoginStartResponse startLogin(@Valid @RequestBody LoginStartRequest request) {
        return authService.startLogin(request);
    }

    @PostMapping("/login/verify")
    public AuthResponse verifyLogin(@Valid @RequestBody LoginVerifyRequest request) {
        return authService.verifyLogin(request);
    }

    @GetMapping("/me")
    public CurrentUserResponse me(@AuthenticationPrincipal Jwt jwt) {
        Number userId = jwt.getClaim("userId");

        return new CurrentUserResponse(
                userId.longValue(),
                jwt.getClaimAsString("username"),
                jwt.getClaimAsString("email"),
                UserRole.valueOf(jwt.getClaimAsString("role"))
        );
    }
}