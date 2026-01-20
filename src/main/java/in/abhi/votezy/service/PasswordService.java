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

    @Autowired
    public PasswordService(PasswordRepository passwordRepository) {
        this.passwordRepository = passwordRepository;
    }

    public Passwords savePassword(Passwords password) {
        Passwords example = new Passwords();
        example.setPassword(password.getPassword());
        if (passwordRepository.exists(Example.of(example))) {
             throw new DuplicateResourceException("Voter with password : "+password.getPassword()+" Already exists");
        }   
        return passwordRepository.save(password);
    }

    public List<Passwords> getAllPasswords(){
        return passwordRepository.findAll();
    }
}
