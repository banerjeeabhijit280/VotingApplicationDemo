package in.abhi.votezy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.abhi.votezy.entity.Candidate;
import in.abhi.votezy.entity.Vote;
import in.abhi.votezy.entity.Voter;
import in.abhi.votezy.exception.DuplicateResourceException;
import in.abhi.votezy.exception.ResourceNotFoundException;
import in.abhi.votezy.repository.CandidateRepository;
import in.abhi.votezy.repository.VoterRepository;
import jakarta.transaction.Transactional;

@Service
public class VoterService {
    private VoterRepository voterRepository;
    private CandidateRepository candidateRepository;

    @Autowired
    public VoterService(VoterRepository voterRepository, CandidateRepository candidateRepository){
        this.voterRepository = voterRepository;
        this.candidateRepository = candidateRepository;
    }

    public Voter registerVoter(Voter voter){
        if (voterRepository.existsByEmail(voter.getEmail()))
        {
            throw new DuplicateResourceException("Voter with email id: "+voter.getEmail()+" Already exists");

        }
        return voterRepository.save(voter);

    }

    public List<Voter>getAllVoters(){
        return voterRepository.findAll();
    }

    public Voter getVoterById(Long id){
        Voter voter=voterRepository.findById(id).orElse(null);
        if (voter==null) {
            throw new ResourceNotFoundException("voter with id: "+ id + " not found");
        }
        return voter;
    }

    public Voter updateVoter(Long id, Voter updatedVoter){
        Voter voter=voterRepository.findById(id).orElse(null);
        if (voter==null) {
            throw new ResourceNotFoundException("voter with id: "+ id + " not found");
        }
        if(updatedVoter.getName()!=null){
            voter.setName(updatedVoter.getName());
        }
       if(updatedVoter.getEmail()!=null){
        voter.setEmail(updatedVoter.getEmail());
       }
        return voterRepository.save(voter);
    }

    @Transactional  //This makes the method atomic. So, if candadate data is saved, the voter data is also saved, else both are reverted(rollbacked).
    public Voter deleteVoter(Long id){
        Voter voter = voterRepository.findById(id).orElse(null);
        if (voter==null) {
            throw new ResourceNotFoundException("Cannot delete voter with id: "+ id + " not found");
        }
        Vote vote = voter.getVote();
        if (vote!=null) {
            Candidate candidate = vote.getCandidate();
            candidate.setVoteCount(candidate.getVoteCount()-1);  //Special delete feature
            candidateRepository.save(candidate);
        }
        voterRepository.delete(voter);
        return voter;
    }
}
