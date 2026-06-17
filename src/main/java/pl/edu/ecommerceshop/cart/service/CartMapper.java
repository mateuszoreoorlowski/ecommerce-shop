package pl.edu.ecommerceshop.cart.service;

import pl.edu.ecommerceshop.cart.dto.CartItemResponse;
import pl.edu.ecommerceshop.cart.dto.CartResponse;
import pl.edu.ecommerceshop.cart.model.Cart;
import pl.edu.ecommerceshop.cart.model.CartItem;

import java.math.BigDecimal;
import java.util.List;

public final class CartMapper {

    private CartMapper() {
    }

    public static CartResponse mapToCartResponse(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream()
                .map(CartMapper::mapToCartItemResponse)
                .toList();
        BigDecimal total = items.stream()
                .map(CartItemResponse::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new CartResponse(
                cart.getId(),
                cart.getCustomerEmail(),
                cart.getStatus(),
                total,
                items,
                cart.getCreatedAt(),
                cart.getUpdatedAt()
        );
    }

    private static CartItemResponse mapToCartItemResponse(CartItem item) {
        return new CartItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getSku(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getUnitPriceSnapshot(),
                item.lineTotal()
        );
    }
}
