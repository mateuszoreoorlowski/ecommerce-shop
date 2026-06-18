package pl.edu.ecommerceshop.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.ecommerceshop.auth.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopflowUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        String normalizedIdentifier = identifier.trim().toLowerCase();

        pl.edu.ecommerceshop.auth.model.User user = userRepository.findByEmailIgnoreCase(normalizedIdentifier)
                .or(() -> userRepository.findByUsernameIgnoreCase(normalizedIdentifier))
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        return User.withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .authorities(List.of(new SimpleGrantedAuthority(user.roleAuthority())))
                .disabled(!user.isActive())
                .accountExpired(false)
                .credentialsExpired(false)
                .build();
    }
}
