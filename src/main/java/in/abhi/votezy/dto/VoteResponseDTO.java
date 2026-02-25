package in.abhi.votezy.dto;

public class VoteResponseDTO {

    private String message;
    private boolean success;
    private Long voterId;
    private Long candidateID;

    public VoteResponseDTO() {
    }

    public VoteResponseDTO(String message, boolean success, Long voterId, Long candidateID) {
        this.message = message;
        this.success = success;
        this.voterId = voterId;
        this.candidateID = candidateID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Long getVoterId() {
        return voterId;
    }

    public void setVoterId(Long voterId) {
        this.voterId = voterId;
    }

    public Long getCandidateID() {
        return candidateID;
    }

    public void setCandidateID(Long candidateID) {
        this.candidateID = candidateID;
    }
}
