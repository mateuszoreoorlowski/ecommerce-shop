package pl.edu.ecommerceshop.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.ecommerceshop.catalog.model.Product;
import pl.edu.ecommerceshop.catalog.repository.ProductRepository;
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

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final StockMovementRepository stockMovementRepository;

    @Transactional
    @Override
    public PaymentResponse processMockPayment(MockPaymentRequest request) {
        Order order = orderRepository.findByIdWithItems(request.orderId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order with id %d not found.".formatted(request.orderId())
                ));

        if (order.getStatus() != OrderStatus.NEW) {
            throw new BusinessException("Only new orders can be paid.");
        }

        Payment payment = paymentRepository.findByOrderId(order.getId())
                .orElseGet(() -> new Payment(order, order.getTotalPrice()));

        if (payment.getStatus() == PaymentStatus.PAID) {
            throw new BusinessException("Payment is already paid.");
        }

        if (Boolean.TRUE.equals(request.success())) {
            commitReservedItems(order);

            payment.markPaid("mock_" + UUID.randomUUID());
            order.markPaymentPaid();
        } else {
            releaseReservedItems(order);

            payment.markFailed("Mock payment rejected by request.");
            order.cancelAfterFailedPayment();
        }

        Payment saved = paymentRepository.save(payment);
        return PaymentMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public PaymentResponse getByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment for order id %d not found.".formatted(orderId)));
        return PaymentMapper.toResponse(payment);
    }

    private void commitReservedItems(Order order) {
        order.getItems().forEach(item -> {
            Product product = productRepository.findByIdForUpdate(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product with id %d not found.".formatted(item.getProductId())));
            product.commitReservedSale(item.getQuantity());
            stockMovementRepository.save(new StockMovement(product.getId(), product.getSku(), StockMovementType.SALE_COMMITTED,
                    item.getQuantity(), "Reserved stock committed after successful payment", order.getOrderNumber()));
        });
    }

    private void releaseReservedItems(Order order) {
        order.getItems().forEach(item -> {
            Product product = productRepository.findByIdForUpdate(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product with id %d not found.".formatted(item.getProductId())));
            product.releaseReservation(item.getQuantity());
            stockMovementRepository.save(new StockMovement(product.getId(), product.getSku(), StockMovementType.RESERVATION_RELEASED,
                    item.getQuantity(), "Reservation released after failed payment", order.getOrderNumber()));
        });
    }
}