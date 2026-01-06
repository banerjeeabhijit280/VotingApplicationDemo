package in.abhi.votezy.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VoteRequestDTO {

    @NotNull(message = "Voter ID is required")
    Long voterId;
    @NotNull(message = "candidate ID is required")
    Long candidateId;
}
