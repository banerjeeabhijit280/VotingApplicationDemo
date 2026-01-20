package in.abhi.votezy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.abhi.votezy.entity.Passwords;
import in.abhi.votezy.service.PasswordService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/voterPassword")
@CrossOrigin
public class VoterPassword {

    private PasswordService passwordService;

    @Autowired
    public VoterPassword(PasswordService passwordService) {
        this.passwordService = passwordService;
    }
 
    @PostMapping("/addPassword")
    public ResponseEntity<Passwords> registerPassword(@RequestBody @Valid Passwords password){ 

    Passwords savedPassword = passwordService.savePassword(password);
    return new ResponseEntity<>(savedPassword, HttpStatus.CREATED);
}

    @GetMapping("/getAllPasswords")
    public ResponseEntity<List<Passwords>> getAllPasswords() {
        List<Passwords> passwords = passwordService.getAllPasswords();
        return new ResponseEntity<>(passwords, HttpStatus.OK);
    }
}