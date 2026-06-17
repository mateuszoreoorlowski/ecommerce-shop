package pl.edu.ecommerceshop.cart.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.edu.ecommerceshop.cart.dto.AddCartItemRequest;
import pl.edu.ecommerceshop.cart.dto.CartCreateRequest;
import pl.edu.ecommerceshop.cart.dto.CartResponse;
import pl.edu.ecommerceshop.cart.dto.UpdateCartItemRequest;
import pl.edu.ecommerceshop.cart.service.CartService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CartResponse create(@Valid @RequestBody CartCreateRequest request) {
        return cartService.createCart(request);
    }

    @GetMapping("/{id}")
    public CartResponse get(@PathVariable Long id) {
        return cartService.getCart(id);
    }

    @PostMapping("/{id}/items")
    public CartResponse addItem(@PathVariable Long id, @Valid @RequestBody AddCartItemRequest request) {
        return cartService.addItem(id, request);
    }

    @PatchMapping("/{cartId}/items/{itemId}")
    public CartResponse updateItem(@PathVariable Long cartId,
                                   @PathVariable Long itemId,
                                   @Valid @RequestBody UpdateCartItemRequest request) {
        return cartService.updateItem(cartId, itemId, request);
    }

    @DeleteMapping("/{cartId}/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItem(@PathVariable Long cartId, @PathVariable Long itemId) {
        cartService.removeItem(cartId, itemId);
    }
}
