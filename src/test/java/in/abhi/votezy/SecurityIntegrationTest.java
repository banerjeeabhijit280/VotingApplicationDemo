package in.abhi.votezy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testUnauthenticatedAccess_ShouldFail() throws Exception {
        mockMvc.perform(get("/api/voters/all"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void testAdminAccessToRegister_ShouldSucceed() throws Exception {
        // We expect 400 Bad Request (Validation error) instead of 403 Forbidden
        // because the body is empty, but access is allowed.
        mockMvc.perform(post("/api/voters/register")
                .contentType("application/json")
                .content("{}")) // Invalid body
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "voter", roles = { "VOTER" })
    void testVoterAccessToRegister_ShouldFail() throws Exception {
        // Voter trying to register a new voter -> Forbidden
        mockMvc.perform(post("/api/voters/register")
                .contentType("application/json")
                .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "voter", roles = { "VOTER" })
    void testVoterAccessToVotes_ShouldSucceed() throws Exception {
        // Accessing votes endpoint -> Allowed (though might return empty list or 400 if
        // param missing)
        mockMvc.perform(get("/api/votes")
                .param("voterId", "1"))
                .andExpect(status().isOk());
    }
}
