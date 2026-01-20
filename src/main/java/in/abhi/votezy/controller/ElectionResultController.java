package in.abhi.votezy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.abhi.votezy.dto.ElectionResultRequestDTO;
import in.abhi.votezy.dto.ElectionResultResponseDTO;
import in.abhi.votezy.entity.ElectionResult;
import in.abhi.votezy.service.ElectionResultService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/election-result")
@CrossOrigin
public class ElectionResultController {
    private ElectionResultService electionResultService;

    @Autowired
    public ElectionResultController(ElectionResultService electionResultService){
        this.electionResultService = electionResultService;
    }

    @PostMapping("/declare")
    public ResponseEntity<ElectionResultResponseDTO> declareElectionResult (@RequestBody @Valid ElectionResultRequestDTO electionResultRequestDTO){
        ElectionResult result = electionResultService.declareElectionResult(electionResultRequestDTO.getElectionName());
        ElectionResultResponseDTO responseDTO = new ElectionResultResponseDTO();
        responseDTO.setElectionName(result.getElectionName());
        responseDTO.setTotalVotes(result.getTotalVotes());
        responseDTO.setWinnerId(result.getWinnerId());
        responseDTO.setWinnerVotes(result.getWinner().getVoteCount());
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/getAllResults")
    public ResponseEntity<List<ElectionResult>> getAllResults(){
        List<ElectionResult> results = electionResultService.getAllElectionResults();
        return ResponseEntity.ok(results);
    }
}
