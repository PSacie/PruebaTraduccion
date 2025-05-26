/*package com.myvideogamelist.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myvideogamelist.backend.dto.GameRatingDTO;
import com.myvideogamelist.backend.dto.ScoreDTO;
import com.myvideogamelist.backend.service.ScoreService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/scores")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ScoreController {
	
	private final ScoreService scoreService;

    @GetMapping("/all")
    public List<ScoreDTO> getAllScores() {
        return scoreService.getAllScores();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScoreDTO> getScoreById(@PathVariable Long id) {
        return scoreService.getScoreById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/game/{gameId}/average")
    public ResponseEntity<Double> getAverageScore(@PathVariable Long gameId) {
        return ResponseEntity.ok(scoreService.getAverageScoreByGame(gameId));
    }

    @GetMapping("/user/{userId}")
    public List<ScoreDTO> getAllScoresByUser(@PathVariable Long userId) {
        return scoreService.getAllScoresByUser(userId);
    }

    @GetMapping("/user/{userId}/latest")
    public List<ScoreDTO> getLatestScoresByUser(@PathVariable Long userId) {
        return scoreService.getLatestScoresByUser(userId);
    }

    @GetMapping("/top-rated")
    public List<GameRatingDTO> getTopRatedGames() {
        return scoreService.getTopRatedGames();
    }
    
    // ScoreController.java
    @GetMapping("/user/{userId}/game/{gameId}")
    public ResponseEntity<ScoreDTO> getUserScoreForGame(
            @PathVariable Long userId, 
            @PathVariable Long gameId) {
        
        return scoreService.getUserScoreForGame(userId, gameId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ScoreDTO> createScore(@RequestBody ScoreDTO dto) {
        return ResponseEntity.ok(scoreService.createScore(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScoreDTO> updateScore(@PathVariable Long id, @RequestBody ScoreDTO dto) {
        return ResponseEntity.ok(scoreService.updateScore(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScore(@PathVariable Long id) {
        scoreService.deleteScore(id);
        return ResponseEntity.noContent().build();
    }

}*/
