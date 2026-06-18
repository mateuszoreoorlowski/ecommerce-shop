package pl.edu.ecommerceshop.cart.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pl.edu.ecommerceshop.cart.dto.AddCartItemRequest;
import pl.edu.ecommerceshop.cart.dto.CartResponse;
import pl.edu.ecommerceshop.cart.dto.UpdateCartItemRequest;
import pl.edu.ecommerceshop.cart.service.CartService;
import pl.edu.ecommerceshop.common.security.SecurityUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CartResponse create(@AuthenticationPrincipal Jwt jwt) {
        return cartService.createCart(SecurityUtils.currentUserEmail(jwt));
    }

    @GetMapping("/{id}")
    public CartResponse get(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return cartService.getCart(id, SecurityUtils.currentUserEmail(jwt), SecurityUtils.isAdmin(jwt));
    }

    @PostMapping("/{id}/items")
    public CartResponse addItem(@PathVariable Long id,
                                @Valid @RequestBody AddCartItemRequest request,
                                @AuthenticationPrincipal Jwt jwt) {
        return cartService.addItem(id, request, SecurityUtils.currentUserEmail(jwt), SecurityUtils.isAdmin(jwt));
    }

    @PatchMapping("/{cartId}/items/{itemId}")
    public CartResponse updateItem(@PathVariable Long cartId,
                                   @PathVariable Long itemId,
                                   @Valid @RequestBody UpdateCartItemRequest request,
                                   @AuthenticationPrincipal Jwt jwt) {
        return cartService.updateItem(cartId, itemId, request, SecurityUtils.currentUserEmail(jwt), SecurityUtils.isAdmin(jwt));
    }

    @DeleteMapping("/{cartId}/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItem(@PathVariable Long cartId, @PathVariable Long itemId, @AuthenticationPrincipal Jwt jwt) {
        cartService.removeItem(cartId, itemId, SecurityUtils.currentUserEmail(jwt), SecurityUtils.isAdmin(jwt));
    }
}