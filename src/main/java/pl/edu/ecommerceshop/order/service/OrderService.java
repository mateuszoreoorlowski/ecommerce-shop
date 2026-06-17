package pl.edu.ecommerceshop.order.service;

import org.springframework.data.domain.Pageable;
import pl.edu.ecommerceshop.common.dto.PageResponse;
import pl.edu.ecommerceshop.order.dto.ChangeOrderStatusRequest;
import pl.edu.ecommerceshop.order.dto.CheckoutRequest;
import pl.edu.ecommerceshop.order.dto.OrderResponse;
import pl.edu.ecommerceshop.order.model.Order;
import pl.edu.ecommerceshop.order.model.OrderStatus;

public interface OrderService {

    OrderResponse checkoutOrder(CheckoutRequest request);

    PageResponse<OrderResponse> getOrdersList(OrderStatus status, Pageable pageable);

    OrderResponse getOrder(Long id);

    OrderResponse cancelOrder(Long id);

    OrderResponse changeStatus(Long id, ChangeOrderStatusRequest request);

    Order findOrder(Long id);
}
