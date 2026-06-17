package pl.edu.ecommerceshop.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressRequest(
        @NotBlank
        @Size(max = 180)
        String street,

        @NotBlank
        @Size(max = 120)
        String city,

        @NotBlank
        @Size(max = 40)
        String postalCode,

        @NotBlank
        @Size(max = 80)
        String country
) {
}
