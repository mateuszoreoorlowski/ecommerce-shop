package pl.edu.ecommerceshop.order.dto;

import pl.edu.ecommerceshop.order.model.OrderStatus;
import pl.edu.ecommerceshop.payment.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        String orderNumber,
        String customerName,
        String customerEmail,
        String customerPhone,
        AddressResponse deliveryAddress,
        OrderStatus status,
        PaymentStatus paymentStatus,
        BigDecimal totalPrice,
        List<OrderItemResponse> items,
        Instant createdAt,
        Instant updatedAt,
        Instant paidAt,
        Instant cancelledAt
) {
}
