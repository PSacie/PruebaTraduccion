package com.myvideogamelist.backend.controller;

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

import com.myvideogamelist.backend.dto.GameListDTO;
import com.myvideogamelist.backend.service.GameListService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lists")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class GameListController {
	
	private final GameListService gameListService;

    @GetMapping
    public List<GameListDTO> getAllLists() {
        return gameListService.getAllLists();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameListDTO> getListById(@PathVariable Long id) {
        return gameListService.getListById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<GameListDTO> createGameList(@RequestBody GameListDTO dto) {
        return ResponseEntity.ok(gameListService.createGameList(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameListDTO> updateGameList(@PathVariable Long id, @RequestBody GameListDTO dto) {
        return ResponseEntity.ok(gameListService.updateGameList(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteList(@PathVariable Long id) {
        gameListService.deleteList(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GameListDTO>> getListsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(gameListService.getListsByUserId(userId));
    }
}
