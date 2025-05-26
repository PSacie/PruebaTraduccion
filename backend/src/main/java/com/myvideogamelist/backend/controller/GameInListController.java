/*package com.myvideogamelist.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myvideogamelist.backend.dto.GameInListDTO;
import com.myvideogamelist.backend.service.GameInListService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/games-in-lists")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class GameInListController {

	private final GameInListService gameInListService;

    @GetMapping("/list/{listId}")
    public ResponseEntity<List<GameInListDTO>> getGamesByListId(@PathVariable Long listId) {
        return ResponseEntity.ok(gameInListService.getGamesByListId(listId));
    }

    @PostMapping
    public ResponseEntity<GameInListDTO> addGameToList(@RequestParam Long listId, @RequestParam Long gameId) {
        return ResponseEntity.ok(gameInListService.addGameToList(listId, gameId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeGameFromList(@PathVariable Long id) {
        gameInListService.removeGameFromList(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsInList(@RequestParam Long listId, @RequestParam Long gameId) {
        return ResponseEntity.ok(gameInListService.existsByListAndGame(listId, gameId));
    }
    
}*/
