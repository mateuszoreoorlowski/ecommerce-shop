package pl.edu.ecommerceshop.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.edu.ecommerceshop.payment.model.Payment;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("""
            SELECT p
            FROM Payment p
            JOIN FETCH p.order o
            WHERE o.id = :orderId
            """)
    Optional<Payment> findByOrderId(@Param("orderId") Long orderId);
}
