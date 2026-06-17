package pl.edu.ecommerceshop.catalog.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.edu.ecommerceshop.catalog.model.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

    @Query("""
            SELECT p
            FROM Product p
            JOIN FETCH p.category
            WHERE p.id = :id
            """)
    Optional<Product> findByIdWithCategory(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT p
            FROM Product p
            JOIN FETCH p.category
            WHERE p.id = :id
            """)
    Optional<Product> findByIdForUpdate(@Param("id") Long id);

    @Query(
            value = """
                    SELECT p
                    FROM Product p
                    JOIN FETCH p.category c
                    WHERE (:query IS NULL
                           OR LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))
                           OR LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%'))
                           OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :query, '%')))
                      AND (:categorySlug IS NULL OR c.slug = :categorySlug)
                      AND (:active IS NULL OR p.active = :active)
                    """,
            countQuery = """
                    SELECT COUNT(p)
                    FROM Product p
                    JOIN p.category c
                    WHERE (:query IS NULL
                           OR LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))
                           OR LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%'))
                           OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :query, '%')))
                      AND (:categorySlug IS NULL OR c.slug = :categorySlug)
                      AND (:active IS NULL OR p.active = :active)
                    """
    )
    Page<Product> searchProducts(
            @Param("query") String query,
            @Param("categorySlug") String categorySlug,
            @Param("active") Boolean active,
            Pageable pageable
    );
}