package pl.edu.ecommerceshop.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pl.edu.ecommerceshop.common.dto.PageResponse;
import pl.edu.ecommerceshop.common.security.SecurityUtils;
import pl.edu.ecommerceshop.order.dto.ChangeOrderStatusRequest;
import pl.edu.ecommerceshop.order.dto.CheckoutRequest;
import pl.edu.ecommerceshop.order.dto.OrderResponse;
import pl.edu.ecommerceshop.order.model.OrderStatus;
import pl.edu.ecommerceshop.order.service.OrderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse checkout(@Valid @RequestBody CheckoutRequest request, @AuthenticationPrincipal Jwt jwt) {
        return orderService.checkoutOrder(
                request,
                SecurityUtils.currentUserEmail(jwt),
                SecurityUtils.isAdmin(jwt)
        );
    }

    @GetMapping
    @Operation(summary = "Get orders list", description = "Returns paginated and filtered order list.")
    public PageResponse<OrderResponse> list(
            @RequestParam(required = false)
            @Parameter(description = "Order status to filter by. Possible values: PENDING, PAID, SHIPPED, DELIVERED, CANCELLED")
            OrderStatus status,

            @ParameterObject
            @PageableDefault(size = 20, sort = "createdAt")
            Pageable pageable
    ) {
        return orderService.getOrdersList(status, pageable);
    }

    @GetMapping("/{id}")
    public OrderResponse get(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    @PostMapping("/{id}/cancel")
    public OrderResponse cancel(@PathVariable Long id) {
        return orderService.cancelOrder(id);
    }

    @PatchMapping("/{id}/status")
    public OrderResponse changeStatus(@PathVariable Long id, @Valid @RequestBody ChangeOrderStatusRequest request) {
        return orderService.changeStatus(id, request);
    }
}
