package pl.edu.ecommerceshop.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.ecommerceshop.cart.model.Cart;
import pl.edu.ecommerceshop.cart.model.CartItem;
import pl.edu.ecommerceshop.cart.service.CartService;
import pl.edu.ecommerceshop.catalog.model.Product;
import pl.edu.ecommerceshop.catalog.repository.ProductRepository;
import pl.edu.ecommerceshop.common.dto.PageResponse;
import pl.edu.ecommerceshop.common.exception.BusinessException;
import pl.edu.ecommerceshop.common.exception.ResourceNotFoundException;
import pl.edu.ecommerceshop.inventory.model.StockMovement;
import pl.edu.ecommerceshop.inventory.model.StockMovementType;
import pl.edu.ecommerceshop.inventory.repository.StockMovementRepository;
import pl.edu.ecommerceshop.order.dto.ChangeOrderStatusRequest;
import pl.edu.ecommerceshop.order.dto.CheckoutRequest;
import pl.edu.ecommerceshop.order.dto.OrderResponse;
import pl.edu.ecommerceshop.order.model.Address;
import pl.edu.ecommerceshop.order.model.Order;
import pl.edu.ecommerceshop.order.model.OrderItem;
import pl.edu.ecommerceshop.order.model.OrderStatus;
import pl.edu.ecommerceshop.order.repository.OrderRepository;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final StockMovementRepository stockMovementRepository;
    private final CartService cartService;
    private final OrderNumberGenerator orderNumberGenerator;

    @Transactional
    @Override
    public OrderResponse checkoutOrder(CheckoutRequest request) {
        Cart cart = cartService.findActiveCart(request.cartId());
        if (cart.getItems().isEmpty()) {
            throw new BusinessException("Cannot checkout an empty cart.");
        }

        Map<Long, Product> lockedProducts = cart.getItems().stream()
                .map(CartItem::getProduct)
                .map(Product::getId)
                .distinct()
                .sorted()
                .map(productId -> productRepository.findByIdForUpdate(productId)
                        .orElseThrow(() -> new ResourceNotFoundException("Product with id %d not found.".formatted(productId))))
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        String orderNumber = orderNumberGenerator.generate();
        Address address = new Address(
                request.deliveryAddress().street(),
                request.deliveryAddress().city(),
                request.deliveryAddress().postalCode(),
                request.deliveryAddress().country()
        );
        Order order = new Order(orderNumber, request.customerName(), request.customerEmail(), request.customerPhone(), address);

        cart.getItems().stream()
                .sorted(Comparator.comparing(item -> item.getProduct().getId()))
                .forEach(cartItem -> {
                    Product product = lockedProducts.get(cartItem.getProduct().getId());
                    product.reserve(cartItem.getQuantity());
                    order.addItem(new OrderItem(order, product.getId(), product.getSku(), product.getName(), cartItem.getQuantity(), product.getPrice()));
                    stockMovementRepository.save(new StockMovement(product.getId(), product.getSku(), StockMovementType.RESERVATION_CREATED,
                            cartItem.getQuantity(), "Stock reserved during checkout", orderNumber));
                });

        cart.markOrdered();
        Order saved = orderRepository.save(order);
        return OrderMapper.mapToOrderResponse(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<OrderResponse> getOrdersList(OrderStatus status, Pageable pageable) {
        Page<OrderResponse> page = status == null
                ? orderRepository.findAll(pageable).map(OrderMapper::mapToOrderResponse)
                : orderRepository.findByStatus(status, pageable).map(OrderMapper::mapToOrderResponse);
        return PageResponse.from(page);
    }

    @Transactional(readOnly = true)
    @Override
    public OrderResponse getOrder(Long id) {
        return OrderMapper.mapToOrderResponse(findOrder(id));
    }

    @Transactional
    @Override
    public OrderResponse cancelOrder(Long id) {
        Order order = findOrder(id);
        order.getItems().forEach(item -> {
            Product product = productRepository.findByIdForUpdate(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product with id %d not found.".formatted(item.getProductId())));
            product.releaseReservation(item.getQuantity());
            stockMovementRepository.save(new StockMovement(product.getId(), product.getSku(), StockMovementType.RESERVATION_RELEASED,
                    item.getQuantity(), "Reservation released after order cancellation", order.getOrderNumber()));
        });
        order.cancel();
        return OrderMapper.mapToOrderResponse(order);
    }

    @Transactional
    @Override
    public OrderResponse changeStatus(Long id, ChangeOrderStatusRequest request) {
        Order order = findOrder(id);
        order.changeStatus(request.status());
        return OrderMapper.mapToOrderResponse(order);
    }

    @Transactional(readOnly = true)
    @Override
    public Order findOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id %d not found.".formatted(id)));
    }
}
