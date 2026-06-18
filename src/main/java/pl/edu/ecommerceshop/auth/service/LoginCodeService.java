package pl.edu.ecommerceshop.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.ecommerceshop.auth.model.User;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoginCodeService {

    private static final String KEY_PREFIX = "auth:login-code:";

    private final StringRedisTemplate stringRedisTemplate;
    private final PasswordEncoder passwordEncoder;
    private final LoginCodeEmailService loginCodeEmailService;

    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${shopflow.security.login-code.ttl:PT5M}")
    private Duration ttl;

    @Value("${shopflow.security.login-code.max-attempts:5}")
    private int maxAttempts;

    public LoginChallenge createChallenge(User user) {
        String challengeId = UUID.randomUUID().toString();
        String code = generateSixDigitCode();
        Instant expiresAt = Instant.now().plus(ttl);

        String key = redisKey(challengeId);

        stringRedisTemplate.opsForHash().putAll(key, Map.of(
                "userId", user.getId().toString(),
                "codeHash", Objects.requireNonNull(passwordEncoder.encode(code)),
                "attempts", "0",
                "expiresAt", expiresAt.toString()
        ));

        stringRedisTemplate.expire(key, ttl);

        loginCodeEmailService.sendLoginCode(user.getEmail(), code, expiresAt);

        return new LoginChallenge(challengeId, maskEmail(user.getEmail()), expiresAt);
    }

    public Long verifyCode(String challengeId, String code) {
        String key = redisKey(challengeId);

        Map<Object, Object> values = stringRedisTemplate.opsForHash().entries(key);
        if (values.isEmpty()) {
            throw new BadCredentialsException("Invalid or expired verification code.");
        }

        int attempts = Integer.parseInt(values.getOrDefault("attempts", "0").toString());

        if (attempts >= maxAttempts) {
            stringRedisTemplate.delete(key);
            throw new BadCredentialsException("Invalid or expired verification code.");
        }

        String codeHash = values.get("codeHash").toString();

        if (!passwordEncoder.matches(code, codeHash)) {
            attempts++;
            stringRedisTemplate.opsForHash().put(key, "attempts", Integer.toString(attempts));

            if (attempts >= maxAttempts) {
                stringRedisTemplate.delete(key);
            }

            throw new BadCredentialsException("Invalid or expired verification code.");
        }

        Long userId = Long.valueOf(values.get("userId").toString());
        stringRedisTemplate.delete(key);

        return userId;
    }

    private String generateSixDigitCode() {
        return "%06d".formatted(secureRandom.nextInt(1_000_000));
    }

    private String redisKey(String challengeId) {
        return KEY_PREFIX + challengeId;
    }

    private String maskEmail(String email) {
        int atIndex = email.indexOf('@');

        if (atIndex <= 1) {
            return "***" + email.substring(atIndex);
        }

        return email.charAt(0) + "***" + email.substring(atIndex);
    }

    public record LoginChallenge(
            String challengeId,
            String maskedEmail,
            Instant expiresAt
    ) {
    }
}