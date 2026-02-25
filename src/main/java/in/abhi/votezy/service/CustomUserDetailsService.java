package in.abhi.votezy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import in.abhi.votezy.entity.Passwords;
import in.abhi.votezy.repository.PasswordRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PasswordRepository passwordRepository;

    @Autowired
    public CustomUserDetailsService(PasswordRepository passwordRepository) {
        this.passwordRepository = passwordRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Treat 'userId' from Passwords entity as 'username'
        Passwords user = passwordRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.builder()
                .username(user.getUserId())
                .password(user.getPassword()) // Encrypted password from DB
                .roles(user.getRole()) // Role (e.g., "ADMIN", "VOTER")
                .build();
    }
}
