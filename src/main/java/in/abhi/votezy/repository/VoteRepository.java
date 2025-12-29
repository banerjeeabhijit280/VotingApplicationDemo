package in.abhi.votezy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.abhi.votezy.entity.Vote;

public interface VoteRepository extends JpaRepository <Vote, Long>{
    
}
