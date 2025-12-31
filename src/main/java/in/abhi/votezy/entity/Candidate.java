package in.abhi.votezy.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generating primary key values.
    private Long id;

    @NotBlank(message = "Name is mandatory")  // Validation to ensure name is not empty.
    private String name;

    @NotBlank(message = "Party is mandatory")  // Validation to ensure party is not empty.
    private String party;

    private int voteCount=0;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL) 
    @JsonIgnore  // This helps the votes to be hidden when called using an external api like bruno
    private List<Vote> votes;   
}
