package in.abhi.votezy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.abhi.votezy.entity.Voter;
import in.abhi.votezy.service.VoterService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/voters")
@CrossOrigin  //This annotation makes sure this controller is accessable from not only postman or api calling apps, but also from frontend sites
public class VoterController {

    private VoterService voterService;
    
    @Autowired
    public VoterController(VoterService voterService){
        this.voterService = voterService;
    }

    @PostMapping("/register")   
    public ResponseEntity<Voter> registerVoter(@RequestBody @Valid Voter voter){
        Voter savedVoter=voterService.registerVoter(voter);
        return new ResponseEntity<>(savedVoter, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Voter>getVoterById(@PathVariable Long id){
        Voter voter=voterService.getVoterById(id);
        return new ResponseEntity<>(voter, HttpStatus.OK);
    }

    //get all voter
    @GetMapping("/all")
    public ResponseEntity<List<Voter>>getAllVoter(){
        List<Voter> voterList =voterService.getAllVoters();
        return new ResponseEntity<>(voterList, HttpStatus.OK);
    }

    //update
    @PutMapping("/update/{id}")
    public ResponseEntity<Voter>updateVoter(@PathVariable Long id,@RequestBody Voter voter){
        Voter updatedVoter = voterService.updateVoter(id, voter);
        return new ResponseEntity<>(updatedVoter, HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteVoter(@PathVariable Long id){
        voterService.deleteVoter(id);
        return new ResponseEntity<>("Voter with id: "+ id + " Is deleted", HttpStatus.OK);
    }
}   