package in.abhi.votezy.dto;

import jakarta.validation.constraints.NotNull;

public class VoteRequestDTO {

    @NotNull(message = "Voter ID is required")
    Long voterId;
    @NotNull(message = "candidate ID is required")
    Long candidateId;

    public VoteRequestDTO() {
    }

    public Long getVoterId() {
        return voterId;
    }

    public void setVoterId(Long voterId) {
        this.voterId = voterId;
    }

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }
}
