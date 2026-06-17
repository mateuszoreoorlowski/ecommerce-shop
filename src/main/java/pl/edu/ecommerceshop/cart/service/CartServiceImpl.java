package pl.edu.ecommerceshop.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.ecommerceshop.cart.dto.AddCartItemRequest;
import pl.edu.ecommerceshop.cart.dto.CartCreateRequest;
import pl.edu.ecommerceshop.cart.dto.CartResponse;
import pl.edu.ecommerceshop.cart.dto.UpdateCartItemRequest;
import pl.edu.ecommerceshop.cart.model.Cart;
import pl.edu.ecommerceshop.cart.model.CartStatus;
import pl.edu.ecommerceshop.cart.repository.CartRepository;
import pl.edu.ecommerceshop.catalog.model.Product;
import pl.edu.ecommerceshop.catalog.repository.ProductRepository;
import pl.edu.ecommerceshop.common.exception.BusinessException;
import pl.edu.ecommerceshop.common.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Transactional
    @Override
    public CartResponse createCart(CartCreateRequest request) {
        Cart cart = new Cart(request.customerEmail());
        return CartMapper.mapToCartResponse(cartRepository.save(cart));
    }

    @Transactional(readOnly = true)
    @Override
    public CartResponse getCart(Long id) {
        return CartMapper.mapToCartResponse(findCart(id));
    }

    @Transactional
    @Override
    public CartResponse addItem(Long cartId, AddCartItemRequest request) {
        Cart cart = findActiveCart(cartId);
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product with id %d not found.".formatted(request.productId())));
        if (!product.isActive()) {
            throw new BusinessException("Cannot add inactive product to cart.");
        }
        cart.addItem(product, request.quantity());
        return CartMapper.mapToCartResponse(cart);
    }

    @Transactional
    @Override
    public CartResponse updateItem(Long cartId, Long itemId, UpdateCartItemRequest request) {
        Cart cart = findActiveCart(cartId);
        cart.updateItemQuantity(itemId, request.quantity());
        return CartMapper.mapToCartResponse(cart);
    }

    @Transactional
    @Override
    public void removeItem(Long cartId, Long itemId) {
        Cart cart = findActiveCart(cartId);
        cart.removeItem(itemId);
    }

    @Transactional(readOnly = true)
    @Override
    public Cart findActiveCart(Long id) {
        return cartRepository.findByIdAndStatus(id, CartStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Active cart with id %d not found.".formatted(id)));
    }

    @Transactional(readOnly = true)
    @Override
    public Cart findCart(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart with id %d not found.".formatted(id)));
    }
}
