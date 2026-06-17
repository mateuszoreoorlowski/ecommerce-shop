package pl.edu.ecommerceshop.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.edu.ecommerceshop.order.model.Order;
import pl.edu.ecommerceshop.order.model.OrderStatus;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNumber(String orderNumber);

    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    @Query(
            value = """
                    SELECT o.id
                    FROM Order o
                    WHERE (:status IS NULL OR o.status = :status)
                    """,
            countQuery = """
                    SELECT COUNT(o)
                    FROM Order o
                    WHERE (:status IS NULL OR o.status = :status)
                    """
    )
    Page<Long> findIdsByStatus(@Param("status") OrderStatus status, Pageable pageable);

    @Query("""
            SELECT DISTINCT o
            FROM Order o
            LEFT JOIN FETCH o.items
            WHERE o.id IN :ids
            """)
    List<Order> findAllWithItemsByIdIn(@Param("ids") Collection<Long> ids);

    @Query("""
            SELECT DISTINCT o
            FROM Order o
            LEFT JOIN FETCH o.items
            WHERE o.id = :id
            """)
    Optional<Order> findByIdWithItems(@Param("id") Long id);
}