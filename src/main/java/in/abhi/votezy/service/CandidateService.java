package in.abhi.votezy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.abhi.votezy.entity.Candidate;
import in.abhi.votezy.entity.Vote;
import in.abhi.votezy.exception.ResourceNotFoundException;
import in.abhi.votezy.repository.CandidateRepository;

@Service
public class CandidateService {
    private CandidateRepository candidateRepository;

    @Autowired
    public CandidateService(CandidateRepository candidateRepository){
        this.candidateRepository = candidateRepository; // Sets the class field to the actual object
    }

    public Candidate addCandidate(Candidate candidate){
        return candidateRepository.save(candidate);
    }

    public List<Candidate> getAllCandadates(){
        return candidateRepository.findAll();
    }

    public Candidate getCandidateById(Long id){
        Candidate candidate = candidateRepository.findById(id).orElse(null);
        if (candidate == null) {
            throw new ResourceNotFoundException("Candidate with id: "+ id +" Doesnot exists");
        }
        return candidate;
    }

    public Candidate updateCandidate(Long id, Candidate updatedCandidate){
        Candidate candidate = getCandidateById(id);
        if (updatedCandidate.getName() != null) {
            candidate.setName(updatedCandidate.getName());
        }
        if (updatedCandidate.getParty() != null) {
            candidate.setParty(updatedCandidate.getParty());
        }
        return candidateRepository.save(candidate);
    }

    public void deleteCandidate(Long id){
        Candidate candidate = getCandidateById(id);
        List<Vote> votes=candidate.getVotes();
        for (Vote v : votes) {
            v.setCandidate(null);
        }
        candidate.getVotes().clear();
        candidateRepository.delete(candidate);

    }
}
