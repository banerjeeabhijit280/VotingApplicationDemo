package in.abhi.votezy.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import in.abhi.votezy.entity.Passwords;


public interface PasswordRepository extends JpaRepository <Passwords, Long>{

}
