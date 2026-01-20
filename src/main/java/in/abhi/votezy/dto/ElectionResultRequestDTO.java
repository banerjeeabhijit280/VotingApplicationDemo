package in.abhi.votezy.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ElectionResultRequestDTO {
    @NotBlank(message = "electionName can't be blank")
    private String electionName;

    
}
