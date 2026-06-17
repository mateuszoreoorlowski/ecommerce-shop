package pl.edu.ecommerceshop.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CheckoutRequest(
        @NotNull
        Long cartId,

        @NotBlank
        @Size(max = 160)
        String customerName,

        @NotBlank
        @Email
        @Size(max = 180)
        String customerEmail,

        @NotBlank
        @Size(max = 40)
        String customerPhone,

        @Valid
        @NotNull
        AddressRequest deliveryAddress
) {
}
