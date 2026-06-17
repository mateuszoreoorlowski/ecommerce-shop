package pl.edu.ecommerceshop.payment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.edu.ecommerceshop.payment.dto.MockPaymentRequest;
import pl.edu.ecommerceshop.payment.dto.PaymentResponse;
import pl.edu.ecommerceshop.payment.service.PaymentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/mock")
    public PaymentResponse mockPayment(@Valid @RequestBody MockPaymentRequest request) {
        return paymentService.processMockPayment(request);
    }

    @GetMapping("/orders/{orderId}")
    public PaymentResponse getByOrderId(@PathVariable Long orderId) {
        return paymentService.getByOrderId(orderId);
    }
}
