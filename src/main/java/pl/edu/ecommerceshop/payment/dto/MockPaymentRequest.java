package pl.edu.ecommerceshop.payment.dto;

import jakarta.validation.constraints.NotNull;

public record MockPaymentRequest(
        @NotNull
        Long orderId,

        @NotNull
        Boolean success
) {
}