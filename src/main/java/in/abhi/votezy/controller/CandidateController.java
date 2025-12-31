package in.abhi.votezy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.abhi.votezy.entity.Candidate;
import in.abhi.votezy.entity.Voter;
import in.abhi.votezy.repository.CandidateRepository;
import in.abhi.votezy.service.CandidateService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/candidate")
@CrossOrigin
public class CandidateController {

    private CandidateService candidateService;

    @Autowired
    public CandidateController(CandidateService candidateService){
        this.candidateService = candidateService;
    } 

    @PostMapping("/add")
    public ResponseEntity<Candidate> addCandidate(@RequestBody @Valid Candidate candidate){
        Candidate savedCandidate = candidateService.addCandidate(candidate);
        return new ResponseEntity<Candidate>(savedCandidate, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Candidate>> getAllCandidates(){
        List<Candidate> candidateList = this.candidateService.getAllCandadates();
        return new ResponseEntity<List<Candidate>>(candidateList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity <Candidate> getCandidate(@PathVariable Long id){
        Candidate candidate = candidateService.getCandidateById(id);
        return new ResponseEntity<Candidate>(candidate, HttpStatus.OK);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity <Candidate> updateCandidate(@PathVariable Long id, @RequestBody Candidate candidate){
        Candidate updatedCandidate = candidateService.updateCandidate(id, candidate);
        return new ResponseEntity<>(updatedCandidate, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCandidate(@PathVariable Long id){
        candidateService.deleteCandidate(id);
        return new ResponseEntity<>("Candidate id: "+id+" deleted successfully", HttpStatus.OK);
    }
}
