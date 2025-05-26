package com.myvideogamelist.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myvideogamelist.backend.dto.GameDTO;
import com.myvideogamelist.backend.dto.TopGameDTO;
import com.myvideogamelist.backend.mapper.GameMapper;
import com.myvideogamelist.backend.service.GameService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class GameController {
	
    private final GameService gameService;

    @GetMapping
    public List<GameDTO> getAllGames() {
        return gameService.getAllGames();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameDTO> getGameById(@PathVariable Long id) {
        return gameService.getGameById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/genres")
    public List<String> getAllGenres() {
        return gameService.getAllGenres();
    }

    @GetMapping("/platforms")
    public List<String> getAllPlatforms() {
        return gameService.getAllPlatforms();
    }
    
    @GetMapping("/random")
    public List<GameDTO> getRandomGames() {
        return gameService.getRandomGames().stream()
            .map(GameMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @GetMapping("/top100")
    public List<TopGameDTO> getTop100Games(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String platform) {
        return gameService.getTop100Games(genre, platform);
    }
    
    @GetMapping("/upcoming")
    public List<GameDTO> getUpcomingGames(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String platform,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        
        return gameService.getUpcomingGames(genre, platform, sortOrder).stream()
                .map(GameMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @GetMapping("/discover")
    public List<GameDTO> getRandomGamesForDiscover(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String platform) {

        return gameService.getRandomGamesForDiscover(genre, platform).stream()
            .map(GameMapper::toDTO)
            .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<GameDTO> createGame(@RequestBody GameDTO dto) {
        return ResponseEntity.ok(gameService.createGame(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameDTO> updateGame(@PathVariable Long id, @RequestBody GameDTO dto) {
        return ResponseEntity.ok(gameService.updateGame(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<GameDTO> searchGamesByTitle(@RequestParam String title) {
        return gameService.searchGamesByTitle(title);
    }

}
