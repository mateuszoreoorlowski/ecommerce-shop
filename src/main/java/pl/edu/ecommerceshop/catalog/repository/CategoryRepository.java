package pl.edu.ecommerceshop.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.ecommerceshop.catalog.model.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsByNameIgnoreCase(String name);
}
