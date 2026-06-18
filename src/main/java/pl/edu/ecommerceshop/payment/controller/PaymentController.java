package pl.edu.ecommerceshop.payment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pl.edu.ecommerceshop.common.security.SecurityUtils;
import pl.edu.ecommerceshop.payment.dto.MockPaymentRequest;
import pl.edu.ecommerceshop.payment.dto.PaymentResponse;
import pl.edu.ecommerceshop.payment.service.PaymentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/mock")
    public PaymentResponse mockPayment(@Valid @RequestBody MockPaymentRequest request, @AuthenticationPrincipal Jwt jwt) {
        return paymentService.processMockPayment(
                request,
                SecurityUtils.currentUserEmail(jwt),
                SecurityUtils.isAdmin(jwt)
        );
    }

    @GetMapping("/orders/{orderId}")
    public PaymentResponse getByOrderId(@PathVariable Long orderId, @AuthenticationPrincipal Jwt jwt) {
        return paymentService.getByOrderId(
                orderId,
                SecurityUtils.currentUserEmail(jwt),
                SecurityUtils.isAdmin(jwt)
        );
    }
}