package pl.edu.ecommerceshop.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginVerifyRequest(
        @NotBlank
        String challengeId,

        @NotBlank
        @Pattern(regexp = "^\\d{6}$", message = "Code must contain exactly 6 digits.")
        String code
) {
}
