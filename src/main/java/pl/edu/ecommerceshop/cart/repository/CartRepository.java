package pl.edu.ecommerceshop.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.ecommerceshop.cart.model.Cart;
import pl.edu.ecommerceshop.cart.model.CartStatus;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByIdAndStatus(Long id, CartStatus status);
}
