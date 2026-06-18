package pl.edu.ecommerceshop.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginStartRequest(
        @NotBlank
        @Size(max = 180)
        String identifier,

        @NotBlank
        @Size(min = 8, max = 72)
        String password
) {
}
