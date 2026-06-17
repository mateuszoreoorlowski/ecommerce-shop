package pl.edu.ecommerceshop.order.dto;

import jakarta.validation.constraints.NotNull;
import pl.edu.ecommerceshop.order.model.OrderStatus;

public record ChangeOrderStatusRequest(
        @NotNull
        OrderStatus status
) {
}
