package pl.edu.ecommerceshop.cart.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CartCreateRequest(
        @NotBlank
        @Email
        @Size(max = 180)
        String customerEmail
) {
}
