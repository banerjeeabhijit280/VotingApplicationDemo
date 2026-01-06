package in.abhi.votezy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.abhi.votezy.entity.Candidate;
import in.abhi.votezy.entity.ElectionResult;
import in.abhi.votezy.exception.ResourceNotFoundException;
import in.abhi.votezy.repository.CandidateRepository;
import in.abhi.votezy.repository.ElectionResultRepository;
import in.abhi.votezy.repository.VoteRepository;

@Service
public class ElectionResultService {
    private CandidateRepository candidateRepository;
    private ElectionResultRepository electionResultRepository;
    private VoteRepository voteRepository;

    @Autowired
    public ElectionResultService(CandidateRepository candidateRepository,
            ElectionResultRepository electionResultRepository, VoteRepository voteRepository) {
        this.candidateRepository = candidateRepository;
        this.electionResultRepository = electionResultRepository;
        this.voteRepository = voteRepository;
    }

    public ElectionResult declareElectionResult(String electionName){
        Optional<ElectionResult> existingResult=this.electionResultRepository.findByElectionName(electionName);
        if(existingResult.isPresent()){
            return existingResult.get();
        }
        if(voteRepository.count()==0){
            throw new IllegalStateException("Cannot declare the result as No votes have been cast yet.");
        }
        List<Candidate>allCandidates=candidateRepository.findAllByOrderByVoteCountDesc();
        if (allCandidates.isEmpty()) {
            throw new ResourceNotFoundException("No candidates available");

        }
        Candidate winner=allCandidates.get(0);
        int totalVotes=0; //(int)voteRepository.count();
        for (Candidate candidate : allCandidates) {
            totalVotes+=candidate.getVoteCount();

        }
        ElectionResult result = new ElectionResult();   
        result.setElectionName(electionName);
        result.setWinner(winner);
        result.setTotalVotes(totalVotes);
        return electionResultRepository.save(result);
    }
    public List<ElectionResult> getAllElectionResults(){
        return electionResultRepository.findAll();
    }
    
}
