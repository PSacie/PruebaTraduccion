/*package com.myvideogamelist.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.myvideogamelist.backend.dto.GameRatingDTO;
import com.myvideogamelist.backend.dto.ScoreDTO;
import com.myvideogamelist.backend.mapper.ScoreMapper;
import com.myvideogamelist.backend.model.Game;
import com.myvideogamelist.backend.model.Score;
import com.myvideogamelist.backend.model.User;
import com.myvideogamelist.backend.repository.GameRepository;
import com.myvideogamelist.backend.repository.ScoreRepository;
import com.myvideogamelist.backend.security.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScoreService {
	
	private final ScoreRepository scoreRepository;
    private final GameRepository gameRepository;

    public List<ScoreDTO> getAllScores() {
        return scoreRepository.findAll().stream()
                .map(ScoreMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<ScoreDTO> getScoreById(Long id) {
        return scoreRepository.findById(id).map(ScoreMapper::toDTO);
    }

    public Double getAverageScoreByGame(Long gameId) {
        return scoreRepository.findAverageScoreByGameId(gameId);
    }

    public List<ScoreDTO> getAllScoresByUser(Long userId) {
        return scoreRepository.findByUserId(userId).stream()
                .map(ScoreMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ScoreDTO> getLatestScoresByUser(Long userId) {
        return scoreRepository.findTop3LatestScoresByUser(userId).stream()
                .map(ScoreMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<GameRatingDTO> getTopRatedGames() {
        List<Object[]> rawData = scoreRepository.findAverageScoresForAllGames();

        return rawData.stream()
            .map(row -> {
                Long gameId = (Long) row[0];
                String title = (String) row[1];
                String coverImage = (String) row[2]; // ✅ Añadido
                Double average = (Double) row[3];
                return new GameRatingDTO(gameId, title, average, coverImage); // ✅ Incluimos la imagen
            })
            .sorted((a, b) -> Double.compare(b.getAverageScore(), a.getAverageScore()))
            .collect(Collectors.toList());
    }
    
    // ScoreService.java
    public Optional<ScoreDTO> getUserScoreForGame(Long userId, Long gameId) {
        return scoreRepository.findByUserIdAndGameId(userId, gameId)
                .map(ScoreMapper::toDTO);
    }

    public ScoreDTO createScore(ScoreDTO dto) {
        User currentUser = SecurityUtils.getAuthenticatedUserOrThrow401();
        Long gameId = dto.getGameId();

        if (scoreRepository.findByUserIdAndGameId(currentUser.getId(), gameId).isPresent()) {
            throw new IllegalStateException("Ya has puntuado este juego. Debes editar tu puntuación.");
        }

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado"));

        Score score = ScoreMapper.toEntity(dto, currentUser, game);
        Score saved = scoreRepository.save(score);
        return ScoreMapper.toDTO(saved);
    }

    public ScoreDTO updateScore(Long id, ScoreDTO dto) {
        User currentUser = SecurityUtils.getAuthenticatedUserOrThrow401();
        Score existing = scoreRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Puntuación no encontrada"));

        if (!existing.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("No tienes permiso para editar esta puntuación");
        }

        if (dto.getScore() != null) {
            existing.setScore(dto.getScore());
        }

        return ScoreMapper.toDTO(scoreRepository.save(existing));
    }

    public void deleteScore(Long id) {
        User currentUser = SecurityUtils.getAuthenticatedUserOrThrow401();
        Score score = scoreRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Puntuación no encontrada"));

        if (!score.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("No tienes permiso para borrar esta puntuación");
        }

        scoreRepository.deleteById(id);
    }

}*/
