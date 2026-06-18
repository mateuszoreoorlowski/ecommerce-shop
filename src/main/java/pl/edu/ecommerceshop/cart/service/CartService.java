package pl.edu.ecommerceshop.cart.service;

import pl.edu.ecommerceshop.cart.dto.AddCartItemRequest;
import pl.edu.ecommerceshop.cart.dto.CartResponse;
import pl.edu.ecommerceshop.cart.dto.UpdateCartItemRequest;
import pl.edu.ecommerceshop.cart.model.Cart;

public interface CartService {

    CartResponse createCart(String customerEmail);

    CartResponse getCart(Long id, String currentUserEmail, boolean admin);

    CartResponse addItem(Long cartId, AddCartItemRequest request, String currentUserEmail, boolean admin);

    CartResponse updateItem(Long cartId, Long itemId, UpdateCartItemRequest request, String currentUserEmail, boolean admin);

    void removeItem(Long cartId, Long itemId, String currentUserEmail, boolean admin);

    Cart findActiveCart(Long id);

    Cart findActiveCart(Long id, String customerEmail);
}