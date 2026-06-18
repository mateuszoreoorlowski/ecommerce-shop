package pl.edu.ecommerceshop.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.ecommerceshop.auth.model.User;
import pl.edu.ecommerceshop.auth.model.UserRole;
import pl.edu.ecommerceshop.auth.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class DefaultUsersInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${shopflow.security.default-users.admin.email:admin@shopflow.local}")
    private String adminEmail;

    @Value("${shopflow.security.default-users.admin.password:admin12345}")
    private String adminPassword;

    @Value("${shopflow.security.default-users.customer.email:customer@shopflow.local}")
    private String customerEmail;

    @Value("${shopflow.security.default-users.customer.password:customer12345}")
    private String customerPassword;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        createUserIfMissing(adminEmail, adminPassword, "System", "Admin", UserRole.ADMIN);
        createUserIfMissing(customerEmail, customerPassword, "Test", "Customer", UserRole.CUSTOMER);
    }

    private void createUserIfMissing(
            String email,
            String rawPassword,
            String firstName,
            String lastName,
            UserRole role
    ) {
        if (userRepository.existsByEmailIgnoreCase(email)) {
            return;
        }

        User user = new User(
                email,
                passwordEncoder.encode(rawPassword),
                firstName,
                lastName,
                role
        );

        userRepository.save(user);
    }
}