package in.abhi.votezy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.abhi.votezy.entity.Candidate;
import java.util.List;


public interface CandidateRepository extends JpaRepository <Candidate, Long> {
    List<Candidate> findAllByOrderByVoteCountDesc();
}
