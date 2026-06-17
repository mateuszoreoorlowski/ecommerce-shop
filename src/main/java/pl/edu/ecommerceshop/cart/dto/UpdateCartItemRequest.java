package pl.edu.ecommerceshop.cart.dto;

import jakarta.validation.constraints.Min;

public record UpdateCartItemRequest(
        @Min(1)
        int quantity
) {
}
