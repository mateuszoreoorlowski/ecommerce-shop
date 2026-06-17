package pl.edu.ecommerceshop.cart.service;

import pl.edu.ecommerceshop.cart.dto.AddCartItemRequest;
import pl.edu.ecommerceshop.cart.dto.CartCreateRequest;
import pl.edu.ecommerceshop.cart.dto.CartResponse;
import pl.edu.ecommerceshop.cart.dto.UpdateCartItemRequest;
import pl.edu.ecommerceshop.cart.model.Cart;

public interface CartService {

    CartResponse createCart(CartCreateRequest request);

    CartResponse getCart(Long id);

    CartResponse addItem(Long cartId, AddCartItemRequest request);

    CartResponse updateItem(Long cartId, Long itemId, UpdateCartItemRequest request);

    void removeItem(Long cartId, Long itemId);

    Cart findActiveCart(Long id);

    Cart findCart(Long id);
}
