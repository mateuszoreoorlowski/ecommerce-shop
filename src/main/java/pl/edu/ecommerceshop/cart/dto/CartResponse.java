package pl.edu.ecommerceshop.cart.dto;

import pl.edu.ecommerceshop.cart.model.CartStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record CartResponse(
        Long id,
        String customerEmail,
        CartStatus status,
        BigDecimal totalPrice,
        List<CartItemResponse> items,
        Instant createdAt,
        Instant updatedAt
) {
}
