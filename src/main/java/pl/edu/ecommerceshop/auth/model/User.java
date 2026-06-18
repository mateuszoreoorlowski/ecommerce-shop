package pl.edu.ecommerceshop.auth.model;

import jakarta.persistence.*;
import lombok.Getter;
import pl.edu.ecommerceshop.common.exception.BusinessException;

import java.time.Instant;

@Entity
@Getter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 180)
    private String email;

    @Column(nullable = false, length = 100)
    private String passwordHash;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private UserRole role;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected User() {
    }

    public User(String email, String passwordHash, String firstName, String lastName, UserRole role) {
        if (email == null || email.isBlank()) {
            throw new BusinessException("User email cannot be blank.");
        }
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new BusinessException("User password hash cannot be blank.");
        }
        if (firstName == null || firstName.isBlank()) {
            throw new BusinessException("User first name cannot be blank.");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new BusinessException("User last name cannot be blank.");
        }
        if (role == null) {
            throw new BusinessException("User role cannot be null.");
        }

        this.email = normalizeEmail(email);
        this.passwordHash = passwordHash;
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.role = role;
        this.active = true;
    }

    @PrePersist
    void prePersist() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = Instant.now();
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    public void changeRole(UserRole role) {
        if (role == null) {
            throw new BusinessException("User role cannot be null.");
        }
        this.role = role;
    }

    public void changePassword(String passwordHash) {
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new BusinessException("User password hash cannot be blank.");
        }
        this.passwordHash = passwordHash;
    }

    public String roleAuthority() {
        return "ROLE_" + role.name();
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }
}
