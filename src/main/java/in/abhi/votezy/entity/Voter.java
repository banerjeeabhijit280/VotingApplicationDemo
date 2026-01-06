package in.abhi.votezy.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.*;
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Voter {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generating primary key values.
    private Long id;

    @NotBlank(message = "Name is mandatory")  // Validation to ensure name is not blank.
    private String name;

    @NotBlank(message = "Email is mandatory") // Validation to ensure email is not blank.
    @Email(message = "Invalid email format")  // Validation to ensure email format is correct.
    private String email;

    private Boolean hasVoted=false;

    @OneToOne(mappedBy = "voter", cascade = CascadeType.ALL) // Bidirectional relationship. It means Vote entity has a field named 'voter' that owns the relationship.
    @JsonIgnore  //This tells jackson to not return this field in the json response 
    private Vote vote;      

}
