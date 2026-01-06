package in.abhi.votezy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import in.abhi.votezy.entity.Candidate;
import in.abhi.votezy.entity.Vote;
import in.abhi.votezy.entity.Voter;
import in.abhi.votezy.exception.ResourceNotFoundException;
import in.abhi.votezy.exception.VoteNotAllowedException;
import in.abhi.votezy.repository.CandidateRepository;
import in.abhi.votezy.repository.VoteRepository;
import in.abhi.votezy.repository.VoterRepository;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.Data;

@Service
@Data
public class VotingService {

    private VoteRepository voteRepository;
    private CandidateRepository candidateRepository;
    private VoterRepository voterRepository;

    public VotingService(VoteRepository voteRepository, CandidateRepository candidateRepository, VoterRepository voterRepository){
        this.voteRepository = voteRepository;
        this.candidateRepository = candidateRepository;
        this.voterRepository = voterRepository;
    }

    @Transactional
    public Vote castVote(Long voterId, Long candidateId){
        if (!voterRepository.existsById(voterId)) {
            throw new ResourceNotFoundException("Voter not found with ID: "+ voterId);
        }
        if (!candidateRepository.existsById(candidateId)) {
            throw new ResourceNotFoundException("candidate not found with ID: "+ candidateId);
        }
        Voter voter = voterRepository.findById(voterId).get();
        if (voter.getHasVoted()) {
            throw new VoteNotAllowedException("Voter ID: "+ voterId + " has already casted the vote");
        }
        Candidate candidate = candidateRepository.findById(candidateId).get();
        Vote vote = new Vote();
        vote.setVoter(voter);
        vote.setCandidate(candidate);
        //voteRepository.save(vote);

        candidate.setVoteCount(candidate.getVoteCount()+1);
        candidateRepository.save(candidate);
        voter.setVote(vote);
        voter.setHasVoted(true);
        voterRepository.save(voter);
        return vote;

    } 

     public List<Vote> getAllVotes(Voter voter){
        return voteRepository.findAll();
    }

}
