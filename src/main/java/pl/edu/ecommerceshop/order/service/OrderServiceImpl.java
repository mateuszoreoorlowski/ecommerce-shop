package pl.edu.ecommerceshop.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import pl.edu.ecommerceshop.payment.model.Payment;
import pl.edu.ecommerceshop.payment.model.PaymentStatus;
import pl.edu.ecommerceshop.payment.repository.PaymentRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;
    private final StockMovementRepository stockMovementRepository;
    private final CartService cartService;
    private final OrderNumberGenerator orderNumberGenerator;

    @Transactional
    @Override
    public OrderResponse checkoutOrder(CheckoutRequest request, String currentUserEmail, boolean admin) {
        Cart cart = admin
                ? cartService.findActiveCart(request.cartId())
                : cartService.findActiveCart(request.cartId(), currentUserEmail);
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
        Order order = new Order(orderNumber, request.customerName(), cart.getCustomerEmail(), request.customerPhone(), address);

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
        Page<Long> orderIdsPage = orderRepository.findIdsByStatus(status, pageable);
        if (orderIdsPage.isEmpty()) {
            Page<OrderResponse> emptyPage = Page.empty(pageable);
            return PageResponse.from(emptyPage);
        }

        Map<Long, Order> ordersById = orderRepository.findAllWithItemsByIdIn(orderIdsPage.getContent()).stream()
                .collect(Collectors.toMap(Order::getId, Function.identity()));

        List<OrderResponse> content = orderIdsPage.getContent().stream()
                .map(ordersById::get)
                .map(OrderMapper::mapToOrderResponse)
                .toList();

        Page<OrderResponse> page = new PageImpl<>(content, pageable, orderIdsPage.getTotalElements());
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
        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            cancelPaidOrderAndRefund(order);
        } else {
            cancelUnpaidOrder(order);
        }
        return OrderMapper.mapToOrderResponse(order);
    }

    @Transactional
    @Override
    public OrderResponse changeStatus(Long id, ChangeOrderStatusRequest request) {
        if (request.status() == OrderStatus.CANCELLED) {
            return cancelOrder(id);
        }

        Order order = findOrder(id);
        if (request.status() == OrderStatus.REFUNDED) {
            refundOrder(order);
        } else {
            order.changeStatus(request.status());
        }
        return OrderMapper.mapToOrderResponse(order);
    }

    @Transactional(readOnly = true)
    @Override
    public Order findOrder(Long id) {
        return orderRepository.findByIdWithItems(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id %d not found.".formatted(id)));
    }

    private void cancelUnpaidOrder(Order order) {
        releaseReservedItems(order);

        paymentRepository.findByOrderId(order.getId())
                .filter(payment -> payment.getStatus() == PaymentStatus.PENDING || payment.getStatus() == PaymentStatus.AUTHORIZED)
                .ifPresent(Payment::cancel);

        order.cancelUnpaid();
    }

    private void cancelPaidOrderAndRefund(Order order) {
        order.cancelPaidAndStartRefund();
        restoreCommittedItems(order);
        refundOrder(order);
    }

    private void refundOrder(Order order) {
        Payment payment = paymentRepository.findByOrderId(order.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment for order id %d not found.".formatted(order.getId())));

        if (payment.getStatus() == PaymentStatus.PAID) {
            payment.startRefund();
        }
        payment.markRefunded();
        order.markRefunded();
    }

    private void releaseReservedItems(Order order) {
        order.getItems().forEach(item -> {
            Product product = productRepository.findByIdForUpdate(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product with id %d not found.".formatted(item.getProductId())));
            product.releaseReservation(item.getQuantity());
            stockMovementRepository.save(new StockMovement(product.getId(), product.getSku(), StockMovementType.RESERVATION_RELEASED,
                    item.getQuantity(), "Reservation released after order cancellation", order.getOrderNumber()));
        });
    }

    private void restoreCommittedItems(Order order) {
        order.getItems().forEach(item -> {
            Product product = productRepository.findByIdForUpdate(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product with id %d not found.".formatted(item.getProductId())));
            product.restoreStockAfterCancellationOrReturn(item.getQuantity());
            stockMovementRepository.save(new StockMovement(product.getId(), product.getSku(), StockMovementType.RETURN_RECEIVED,
                    item.getQuantity(), "Stock restored after paid order cancellation", order.getOrderNumber()));
        });
    }
}