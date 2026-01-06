package in.abhi.votezy.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.*;
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Vote {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "voter_id", unique = true)   
    @JsonIgnore
    private Voter voter;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    @JsonIgnore
    private Candidate candidate;

    @JsonProperty("voterId")          //Forces jackson to call these methods
    public Long getVoterId(){           //We are adding this part as to return atleast the voter ID as all other fields are marked JsonIgnore except id.
        return voter!=null ? voter.getId():null;
    }

    @JsonProperty("candidateId")
    public Long getCandidateId(){           //We are adding this part as to return atleast the voter ID as all other fields are marked JsonIgnore except id.
        return candidate!=null ? candidate.getId():null;
    }
}
