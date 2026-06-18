package pl.edu.ecommerceshop.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.ecommerceshop.cart.dto.AddCartItemRequest;
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
    public CartResponse createCart(String customerEmail) {
        Cart cart = new Cart(normalizeEmail(customerEmail));
        return CartMapper.mapToCartResponse(cartRepository.save(cart));
    }

    @Transactional(readOnly = true)
    @Override
    public CartResponse getCart(Long id, String currentUserEmail, boolean admin) {
        Cart cart = findCartForCurrentUser(id, currentUserEmail, admin);
        return CartMapper.mapToCartResponse(cart);
    }

    @Transactional
    @Override
    public CartResponse addItem(Long cartId, AddCartItemRequest request, String currentUserEmail, boolean admin) {
        Cart cart = findActiveCartForCurrentUser(cartId, currentUserEmail, admin);

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id %d not found.".formatted(request.productId())
                ));

        if (!product.isActive()) {
            throw new BusinessException("Cannot add inactive product to cart.");
        }

        cart.addItem(product, request.quantity());
        return CartMapper.mapToCartResponse(cart);
    }

    @Transactional
    @Override
    public CartResponse updateItem(Long cartId, Long itemId, UpdateCartItemRequest request, String currentUserEmail, boolean admin) {
        Cart cart = findActiveCartForCurrentUser(cartId, currentUserEmail, admin);
        cart.updateItemQuantity(itemId, request.quantity());
        return CartMapper.mapToCartResponse(cart);
    }

    @Transactional
    @Override
    public void removeItem(Long cartId, Long itemId, String currentUserEmail, boolean admin) {
        Cart cart = findActiveCartForCurrentUser(cartId, currentUserEmail, admin);
        cart.removeItem(itemId);
    }

    @Transactional(readOnly = true)
    @Override
    public Cart findActiveCart(Long id) {
        return findActiveCartForAdmin(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Cart findActiveCart(Long id, String customerEmail) {
        return findActiveCartForCustomer(id, customerEmail);
    }

    private Cart findActiveCartForCurrentUser(Long id, String currentUserEmail, boolean admin) {
        if (admin) {
            return findActiveCartForAdmin(id);
        }

        return findActiveCartForCustomer(id, currentUserEmail);
    }

    private Cart findCartForCurrentUser(Long id, String currentUserEmail, boolean admin) {
        if (admin) {
            return findCartForAdmin(id);
        }

        return findCartForCustomer(id, currentUserEmail);
    }

    private Cart findActiveCartForAdmin(Long id) {
        return cartRepository.findByIdAndStatusWithItemsAndProducts(id, CartStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Active cart with id %d not found.".formatted(id)
                ));
    }

    private Cart findActiveCartForCustomer(Long id, String customerEmail) {
        return cartRepository.findByIdAndStatusAndCustomerEmailWithItemsAndProducts(
                        id,
                        CartStatus.ACTIVE,
                        normalizeEmail(customerEmail)
                )
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Active cart with id %d not found.".formatted(id)
                ));
    }

    private Cart findCartForAdmin(Long id) {
        return cartRepository.findByIdWithItemsAndProducts(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cart with id %d not found.".formatted(id)
                ));
    }

    private Cart findCartForCustomer(Long id, String customerEmail) {
        return cartRepository.findByIdAndCustomerEmailWithItemsAndProducts(
                        id,
                        normalizeEmail(customerEmail)
                )
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cart with id %d not found.".formatted(id)
                ));
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new BusinessException("Customer email cannot be blank.");
        }

        return email.trim().toLowerCase();
    }
}