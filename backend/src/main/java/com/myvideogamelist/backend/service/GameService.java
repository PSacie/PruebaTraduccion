/*package com.myvideogamelist.backend.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.myvideogamelist.backend.dto.GameDTO;
import com.myvideogamelist.backend.dto.TopGameDTO;
import com.myvideogamelist.backend.mapper.GameMapper;
import com.myvideogamelist.backend.model.Game;
import com.myvideogamelist.backend.model.User;
import com.myvideogamelist.backend.repository.GameRepository;
import com.myvideogamelist.backend.repository.ScoreRepository;
import com.myvideogamelist.backend.security.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameService {
	
	private final GameRepository gameRepository;
    private final ScoreRepository scoreRepository;

    public List<GameDTO> getAllGames() {
        return gameRepository.findAll().stream()
                .map(game -> {
                    Double avg = scoreRepository.findAverageScoreByGameId(game.getId());
                    return GameMapper.toDTO(game, avg);
                })
                .collect(Collectors.toList());
    }

    public Optional<GameDTO> getGameById(Long id) {
        return gameRepository.findById(id)
                .map(game -> {
                    Double avg = scoreRepository.findAverageScoreByGameId(game.getId());
                    return GameMapper.toDTO(game, avg);
                });
    }
    
    public List<Game> getRandomGames() {
        return gameRepository.findRandomGames();
    }

    public List<GameDTO> searchGamesByTitle(String title) {
        return gameRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(game -> {
                    Double avg = scoreRepository.findAverageScoreByGameId(game.getId());
                    return GameMapper.toDTO(game, avg);
                })
                .collect(Collectors.toList());
    }

    public GameDTO createGame(GameDTO dto) {
        User currentUser = SecurityUtils.getAuthenticatedUserOrThrow401();

        if (!currentUser.getRole().getName().equalsIgnoreCase("admin")) {
            throw new AccessDeniedException("Solo los administradores pueden crear juegos.");
        }

        boolean exists = gameRepository.findByTitleContainingIgnoreCase(dto.getTitle())
                .stream()
                .anyMatch(g -> g.getTitle().equalsIgnoreCase(dto.getTitle()));

        if (exists) {
            throw new IllegalArgumentException("Ya existe un juego con ese título.");
        }

        Game game = GameMapper.toEntity(dto);
        Game saved = gameRepository.save(game);
        Double avg = scoreRepository.findAverageScoreByGameId(saved.getId());
        return GameMapper.toDTO(saved, avg);
    }

    public GameDTO updateGame(Long id, GameDTO dto) {
        User currentUser = SecurityUtils.getAuthenticatedUserOrThrow401();

        if (!currentUser.getRole().getName().equalsIgnoreCase("admin")) {
            throw new AccessDeniedException("Solo los administradores pueden editar juegos.");
        }

        Game existingGame = gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado"));

        if (dto.getTitle() != null) existingGame.setTitle(dto.getTitle());
        if (dto.getGenre() != null) existingGame.setGenre(dto.getGenre());
        if (dto.getPlatform() != null) existingGame.setPlatform(dto.getPlatform());
        if (dto.getReleaseDate() != null) existingGame.setReleaseDate(dto.getReleaseDate());
        if (dto.getDescription() != null) existingGame.setDescription(dto.getDescription());
        if (dto.getCoverImage() != null) existingGame.setCoverImage(dto.getCoverImage());

        Game updated = gameRepository.save(existingGame);
        Double avg = scoreRepository.findAverageScoreByGameId(updated.getId());
        return GameMapper.toDTO(updated, avg);
    }

    public void deleteGame(Long id) {
        User currentUser = SecurityUtils.getAuthenticatedUserOrThrow401();

        if (!currentUser.getRole().getName().equalsIgnoreCase("admin")) {
            throw new AccessDeniedException("Solo los administradores pueden eliminar juegos.");
        }

        gameRepository.deleteById(id);
    }
    
    // Obtener todos los géneros de forma única
    public List<String> getAllGenres() {
        Set<String> genreSet = new HashSet<>();

        for (Game game : gameRepository.findAll()) {
            if (game.getGenre() != null) {
                String[] genres = game.getGenre().split(", ");
                for (String genre : genres) {
                    if (!genre.trim().isEmpty()) {
                        genreSet.add(genre.trim());
                    }
                }
            }
        }

        List<String> sortedGenres = new ArrayList<>(genreSet);
        sortedGenres.sort(String::compareTo);
        return sortedGenres;
    }

    // Obtener todas las plataformas de forma única
    public List<String> getAllPlatforms() {
        Set<String> platformSet = new HashSet<>();

        for (Game game : gameRepository.findAll()) {
            if (game.getPlatform() != null) {
                String[] platforms = game.getPlatform().split(", ");
                for (String platform : platforms) {
                    if (!platform.trim().isEmpty()) {
                        platformSet.add(platform.trim());
                    }
                }
            }
        }

        List<String> sortedPlatforms = new ArrayList<>(platformSet);
        sortedPlatforms.sort(String::compareTo);
        return sortedPlatforms;
    }
    
    public List<TopGameDTO> getTop100Games(String genre, String platform) {
        List<TopGameDTO> topRated = scoreRepository.findTopRatedGamesWithFilters(genre, platform);

        if (topRated.size() >= 100) {
            return topRated.subList(0, 100);
        }

        int missing = 100 - topRated.size();
        List<Game> fillerGames = gameRepository.findGamesWithoutScoresWithFilters(genre, platform);

        List<TopGameDTO> result = new ArrayList<>(topRated);

        for (Game game : fillerGames) {
            boolean alreadyIncluded = result.stream()
                .anyMatch(g -> g.getGameId().equals(game.getId()));

            if (!alreadyIncluded) {
                result.add(new TopGameDTO(
                    game.getId(),
                    game.getTitle(),
                    game.getGenre(),
                    game.getPlatform(),
                    game.getCoverImage(),
                    null
                ));
            }

            if (result.size() == 100) break;
        }

        return result;
    }
    
    public List<Game> getUpcomingGames(String genre, String platform, String sortOrder) {
        List<Game> games = gameRepository.findUpcomingGamesWithFilters(
            genre != null && !genre.isEmpty() ? genre : null,
            platform != null && !platform.isEmpty() ? platform : null
        );

        if ("desc".equalsIgnoreCase(sortOrder)) {
            games.sort(Comparator.comparing(Game::getReleaseDate).reversed());
        } else {
            games.sort(Comparator.comparing(Game::getReleaseDate));
        }

        return games;
    }
    
    public List<Game> getRandomGamesForDiscover(String genre, String platform) {
        String genreValue = (genre != null && !genre.isEmpty()) ? genre : null;
        String platformValue = (platform != null && !platform.isEmpty()) ? platform : null;

        return gameRepository.findRandomGamesWithFilters(genreValue, platformValue);
    }

}*/
