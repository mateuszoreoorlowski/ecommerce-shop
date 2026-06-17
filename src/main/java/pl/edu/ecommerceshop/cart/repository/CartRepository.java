package pl.edu.ecommerceshop.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.edu.ecommerceshop.cart.model.Cart;
import pl.edu.ecommerceshop.cart.model.CartStatus;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByIdAndStatus(Long id, CartStatus status);

    @Query("""
            SELECT DISTINCT c
            FROM Cart c
            LEFT JOIN FETCH c.items i
            LEFT JOIN FETCH i.product
            WHERE c.id = :id
            """)
    Optional<Cart> findByIdWithItemsAndProducts(@Param("id") Long id);

    @Query("""
            SELECT DISTINCT c
            FROM Cart c
            LEFT JOIN FETCH c.items i
            LEFT JOIN FETCH i.product
            WHERE c.id = :id AND c.status = :status
            """)
    Optional<Cart> findByIdAndStatusWithItemsAndProducts(@Param("id") Long id, @Param("status") CartStatus status);
}