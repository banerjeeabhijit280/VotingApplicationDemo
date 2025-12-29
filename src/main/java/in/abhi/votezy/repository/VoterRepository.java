package in.abhi.votezy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.abhi.votezy.entity.Voter;

public interface VoterRepository extends JpaRepository <Voter, Long>{
    boolean existsByEmail(String email);
}
