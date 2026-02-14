package in.abhi.votezy.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import in.abhi.votezy.entity.Voter;
import in.abhi.votezy.exception.DuplicateResourceException;
import in.abhi.votezy.exception.ResourceNotFoundException;
import in.abhi.votezy.repository.CandidateRepository;
import in.abhi.votezy.repository.VoterRepository;

/** -----------------------------------------------------------
@Mock -> Creates a fake object (e.g., a fake VoterRepository).
@InjectMocks -> Creates an instance of the class we are testing
              (VoterService)
              and injects the @Mock objects into it.
@Test -> Marks a method as a test case.
----------------------------------------------------------------
 */

@ExtendWith(MockitoExtension.class) 
public class VoterServiceTest {

    @Mock
    private VoterRepository voterRepository;

   
    @Mock
    private CandidateRepository candidateRepository;

   
    @InjectMocks
    private VoterService voterService;

    /**
     Test Case 1: Register a Voter Successfully
     Scenario: A new user wants to register. The email does not exist in DB java.
     */
    @Test
    void testRegisterVoter_Success() {
       
        Voter voter = new Voter();
        voter.setName("Test User");
        voter.setEmail("test@example.com");
        when(voterRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(voterRepository.save(any(Voter.class))).thenReturn(voter);
        Voter savedVoter = voterService.registerVoter(voter);
        assertNotNull(savedVoter);
        assertEquals("Test User", savedVoter.getName());
        verify(voterRepository, times(1)).existsByEmail("test@example.com");
        verify(voterRepository, times(1)).save(voter);
    }

    /**
     Test Case 2: Register Voter fails due to Duplicate Email
     Scenario: A user tries to register with an email that is already taken.
     */
    @Test
    void testRegisterVoter_DuplicateEmail() {
        // --- ARRANGE ---
        Voter voter = new Voter();
        voter.setEmail("existing@example.com");

        // Teach Mock: "The email ALREADY exists."
        when(voterRepository.existsByEmail("existing@example.com")).thenReturn(true);

        // --- ACT & ASSERT ---
        // We expect the service to throw a 'DuplicateResourceException'.
        // assertThrows(ExceptionClass.class, () -> methodCall());
        assertThrows(DuplicateResourceException.class, () -> {
            voterService.registerVoter(voter);
        });

        // Verify we NEVER called save(), because exception happened before that.
        verify(voterRepository, times(0)).save(any(Voter.class));
    }

    /**
    Test Case 3: Get Voter by ID - Success
     */
    @Test
    void testGetVoterById_Success() {
        long voterId = 1L;
        Voter mockVoter = new Voter();
        mockVoter.setId(voterId);
        mockVoter.setName("Alice");
        when(voterRepository.findById(voterId)).thenReturn(Optional.of(mockVoter));
        Voter foundVoter = voterService.getVoterById(voterId);
        assertNotNull(foundVoter);
        assertEquals("Alice", foundVoter.getName());
        assertEquals(1L, foundVoter.getId());
    }

    /**
    Test Case 4: Get Voter by ID - Not Found
     */
    @Test
    void testGetVoterById_NotFound() {
        long nonExistentId = 99L;
        when(voterRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            voterService.getVoterById(nonExistentId);
        });
        assertNotNull(exception);
    }

    /**
    Test Case 5: Get All Voters
     */
    @Test
    void testGetAllVoters() {
        // --- ARRANGE ---
        Voter v1 = new Voter();
        v1.setName("Alice");
        Voter v2 = new Voter();
        v2.setName("Bob");
        List<Voter> mockList = Arrays.asList(v1, v2);
        when(voterRepository.findAll()).thenReturn(mockList);
        List<Voter> result = voterService.getAllVoters();
        assertEquals(2, result.size()); // Expect 2 items
        assertEquals("Alice", result.get(0).getName());
    }

    /**
    Test Case 6: Delete Voter
    (We are testing the happy path where voter has NOT voted yet for simplicity)
     */
    @Test
    void testDeleteVoter_Success() {
        long voterId = 1L;
        Voter voter = new Voter();
        voter.setId(voterId);
        when(voterRepository.findById(voterId)).thenReturn(Optional.of(voter));
        voterService.deleteVoter(voterId);
        verify(voterRepository, times(1)).delete(voter);
    }
}
