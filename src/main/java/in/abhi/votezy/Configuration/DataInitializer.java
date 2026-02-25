package in.abhi.votezy.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import in.abhi.votezy.entity.Passwords;
import in.abhi.votezy.repository.PasswordRepository;

@Configuration
public class DataInitializer {

    @Autowired
    private PasswordRepository passwordRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // Check if admin exists
            if (passwordRepository.findByUserId("admin").isEmpty()) {
                Passwords admin = new Passwords();
                admin.setUserId("admin");
                admin.setPassword(passwordEncoder.encode("admin123")); // Default password
                admin.setRole("ADMIN");
                passwordRepository.save(admin);
                System.out.println("DEFAULT ADMIN USER CREATED: username=admin, password=admin123");
            }
        };
    }
}
