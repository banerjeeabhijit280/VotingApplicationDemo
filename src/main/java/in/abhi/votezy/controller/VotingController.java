package in.abhi.votezy.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.abhi.votezy.dto.VoteRequestDTO;
import in.abhi.votezy.dto.VoteResponseDTO;
import in.abhi.votezy.entity.Vote;
import in.abhi.votezy.entity.Voter;
import in.abhi.votezy.service.VotingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/votes")
@CrossOrigin
public class VotingController {
    private VotingService votingService;

    @Autowired
    public VotingController(VotingService votingService){
        this.votingService = votingService; 
    }

    @PostMapping("/cast")
    public ResponseEntity<VoteResponseDTO> castVote(@RequestBody @Valid VoteRequestDTO voteRequest){
        Vote vote = votingService.castVote(voteRequest.getVoterId(), voteRequest.getCandidateId());
        VoteResponseDTO voteResponse = new VoteResponseDTO("Vote caseted successfully", true, vote.getVoterId(), vote.getCandidateId());
        return new ResponseEntity<>(voteResponse, HttpStatus.CREATED);
        
    }

    @GetMapping
    public ResponseEntity<List<Vote>> getAllVotes(@RequestParam Long voterId){
        Voter voter = new Voter();
        voter.setId(voterId);
        List <Vote>voteList=votingService.getAllVotes(voter);
        return new ResponseEntity<>(voteList, HttpStatus.OK);
    }
}
 