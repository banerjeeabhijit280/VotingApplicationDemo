package in.abhi.votezy.dto;

import jakarta.validation.constraints.NotBlank;

public class ElectionResultRequestDTO {
    @NotBlank(message = "electionName can't be blank")
    private String electionName;

    public String getElectionName() {
        return electionName;
    }

    public void setElectionName(String electionName) {
        this.electionName = electionName;
    }
}
