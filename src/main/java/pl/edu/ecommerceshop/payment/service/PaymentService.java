package pl.edu.ecommerceshop.payment.service;

import pl.edu.ecommerceshop.payment.dto.MockPaymentRequest;
import pl.edu.ecommerceshop.payment.dto.PaymentResponse;

public interface PaymentService {

    PaymentResponse processMockPayment(MockPaymentRequest request, String currentUserEmail, boolean admin);

    PaymentResponse getByOrderId(Long orderId, String currentUserEmail, boolean admin);
}