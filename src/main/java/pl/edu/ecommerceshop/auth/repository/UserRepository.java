package pl.edu.ecommerceshop.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.ecommerceshop.auth.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);
}