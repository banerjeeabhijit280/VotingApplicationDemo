package in.abhi.votezy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import in.abhi.votezy.entity.Passwords;
import in.abhi.votezy.exception.DuplicateResourceException;
import in.abhi.votezy.repository.PasswordRepository;

@Service
public class PasswordService {
    private final PasswordRepository passwordRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordService(PasswordRepository passwordRepository,
            org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.passwordRepository = passwordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Passwords savePassword(Passwords password) {
        Passwords example = new Passwords();
        example.setUserId(password.getUserId()); // Check by UserId, not password!
        if (passwordRepository.exists(Example.of(example))) {
            throw new DuplicateResourceException("Voter with userId : " + password.getUserId() + " Already exists");
        }

        // Encrypt password before saving
        password.setPassword(passwordEncoder.encode(password.getPassword()));

        // Default role if not provided
        if (password.getRole() == null || password.getRole().isEmpty()) {
            password.setRole("VOTER");
        }

        return passwordRepository.save(password);
    }

    public List<Passwords> getAllPasswords() {
        return passwordRepository.findAll();
    }
}
