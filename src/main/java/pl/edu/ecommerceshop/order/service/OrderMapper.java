package pl.edu.ecommerceshop.order.service;

import pl.edu.ecommerceshop.order.dto.AddressResponse;
import pl.edu.ecommerceshop.order.dto.OrderItemResponse;
import pl.edu.ecommerceshop.order.dto.OrderResponse;
import pl.edu.ecommerceshop.order.model.Address;
import pl.edu.ecommerceshop.order.model.Order;
import pl.edu.ecommerceshop.order.model.OrderItem;

public final class OrderMapper {

    private OrderMapper() {
    }

    public static OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getCustomerName(),
                order.getCustomerEmail(),
                order.getCustomerPhone(),
                mapToAddressResponse(order.getDeliveryAddress()),
                order.getStatus(),
                order.getPaymentStatus(),
                order.getTotalPrice(),
                order.getItems().stream().map(OrderMapper::mapToOrderItemResponse).toList(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                order.getPaidAt(),
                order.getCancelledAt()
        );
    }

    private static AddressResponse mapToAddressResponse(Address address) {
        return new AddressResponse(address.getStreet(), address.getCity(), address.getPostalCode(), address.getCountry());
    }

    private static OrderItemResponse mapToOrderItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getProductId(),
                item.getProductSku(),
                item.getProductName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getLineTotal()
        );
    }
}
