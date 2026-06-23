package pl.edu.ecommerceshop.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.ecommerceshop.catalog.model.Product;
import pl.edu.ecommerceshop.catalog.repository.ProductRepository;
import pl.edu.ecommerceshop.common.events.DomainEventPublisher;
import pl.edu.ecommerceshop.common.events.PaymentCompletedEvent;
import pl.edu.ecommerceshop.common.events.PaymentFailedEvent;
import pl.edu.ecommerceshop.common.exception.BusinessException;
import pl.edu.ecommerceshop.common.exception.ResourceNotFoundException;
import pl.edu.ecommerceshop.inventory.model.StockMovement;
import pl.edu.ecommerceshop.inventory.model.StockMovementType;
import pl.edu.ecommerceshop.inventory.repository.StockMovementRepository;
import pl.edu.ecommerceshop.order.model.Order;
import pl.edu.ecommerceshop.order.model.OrderStatus;
import pl.edu.ecommerceshop.order.repository.OrderRepository;
import pl.edu.ecommerceshop.payment.dto.MockPaymentRequest;
import pl.edu.ecommerceshop.payment.dto.PaymentResponse;
import pl.edu.ecommerceshop.payment.model.Payment;
import pl.edu.ecommerceshop.payment.model.PaymentStatus;
import pl.edu.ecommerceshop.payment.repository.PaymentRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final String MOCK_PAYMENT_FAILURE_REASON = "Mock payment rejected by request.";

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final StockMovementRepository stockMovementRepository;
    private final DomainEventPublisher domainEventPublisher;

    @Transactional
    @Override
    public PaymentResponse processMockPayment(MockPaymentRequest request, String currentUserEmail, boolean admin) {
        Order order = findOrderForPayment(request.orderId(), currentUserEmail, admin);

        if (order.getStatus() != OrderStatus.NEW) {
            throw new BusinessException("Only new orders can be paid.");
        }

        Payment payment = paymentRepository.findByOrderId(order.getId())
                .orElseGet(() -> new Payment(order, order.getTotalPrice()));

        if (payment.getStatus() == PaymentStatus.PAID) {
            throw new BusinessException("Payment is already paid.");
        }

        if (Boolean.TRUE.equals(request.success())) {
            processSuccessfulPayment(order, payment);
        } else {
            processFailedPayment(order, payment);
        }

        Payment savedPayment = paymentRepository.save(payment);
        return PaymentMapper.toResponse(savedPayment);
    }

    @Transactional(readOnly = true)
    @Override
    public PaymentResponse getByOrderId(Long orderId, String currentUserEmail, boolean admin) {
        Payment payment = findPaymentForOrder(orderId, currentUserEmail, admin)
                .orElseThrow(() -> new ResourceNotFoundException("Payment for order id %d not found.".formatted(orderId)));

        return PaymentMapper.toResponse(payment);
    }

    private void processSuccessfulPayment(Order order, Payment payment) {
        commitReservedItems(order);

        payment.markPaid("mock_" + UUID.randomUUID());
        order.markPaymentPaid();

        domainEventPublisher.publish(
                new PaymentCompletedEvent(
                        order.getId(),
                        order.getOrderNumber(),
                        order.getCustomerEmail(),
                        payment.getAmount()
                )
        );
    }

    private void processFailedPayment(Order order, Payment payment) {
        releaseReservedItems(order);

        payment.markFailed(MOCK_PAYMENT_FAILURE_REASON);
        order.cancelAfterFailedPayment();

        domainEventPublisher.publish(
                new PaymentFailedEvent(
                        order.getId(),
                        order.getOrderNumber(),
                        order.getCustomerEmail(),
                        payment.getAmount(),
                        MOCK_PAYMENT_FAILURE_REASON
                )
        );
    }

    private Order findOrderForPayment(
            Long orderId,
            String currentUserEmail,
            boolean admin
    ) {
        if (admin) {
            return orderRepository.findByIdWithItems(orderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Order with id %d not found.".formatted(orderId)));
        }

        return orderRepository.findByIdAndCustomerEmailWithItems(orderId, normalizeEmail(currentUserEmail))
                .orElseThrow(() -> new ResourceNotFoundException("Order with id %d not found.".formatted(orderId)));
    }

    private Optional<Payment> findPaymentForOrder(Long orderId, String currentUserEmail, boolean admin) {
        if (admin) {
            return paymentRepository.findByOrderId(orderId);
        }

        return paymentRepository.findByOrderIdAndCustomerEmail(orderId, normalizeEmail(currentUserEmail));
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new BusinessException("Customer email cannot be blank.");
        }

        return email.trim().toLowerCase();
    }

    private void commitReservedItems(Order order) {
        order.getItems().forEach(item -> {
            Product product = productRepository.findByIdForUpdate(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product with id %d not found.".formatted(item.getProductId())));

            product.commitReservedSale(item.getQuantity());

            stockMovementRepository.save(
                    new StockMovement(
                            product.getId(),
                            product.getSku(),
                            StockMovementType.SALE_COMMITTED,
                            item.getQuantity(),
                            "Reserved stock committed after successful payment",
                            order.getOrderNumber()
                    )
            );
        });
    }

    private void releaseReservedItems(Order order) {
        order.getItems().forEach(item -> {
            Product product = productRepository.findByIdForUpdate(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product with id %d not found.".formatted(item.getProductId())));

            product.releaseReservation(item.getQuantity());

            stockMovementRepository.save(
                    new StockMovement(
                            product.getId(),
                            product.getSku(),
                            StockMovementType.RESERVATION_RELEASED,
                            item.getQuantity(),
                            "Reservation released after failed payment",
                            order.getOrderNumber()
                    )
            );
        });
    }
}