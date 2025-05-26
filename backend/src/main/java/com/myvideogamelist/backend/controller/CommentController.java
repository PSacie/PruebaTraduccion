package com.myvideogamelist.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myvideogamelist.backend.dto.CommentDTO;
import com.myvideogamelist.backend.model.User;
import com.myvideogamelist.backend.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CommentController {
	
	private final CommentService commentService;

    @GetMapping
    public List<CommentDTO> getAllComments() {
        return commentService.getAllComments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long id) {
        return commentService.getCommentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/game/{gameId}")
    public List<CommentDTO> getCommentsByGame(@PathVariable Long gameId) {
        return commentService.getCommentsByGame(gameId);
    }

    @GetMapping("/user/{userId}")
    public List<CommentDTO> getAllCommentsByUser(@PathVariable Long userId) {
        return commentService.getAllCommentsByUser(userId);
    }

    @GetMapping("/user/{userId}/latest")
    public List<CommentDTO> getLatestCommentsByUser(@PathVariable Long userId) {
        return commentService.getLatestCommentsByUser(userId);
    }
    
    @GetMapping("/latest")
    public List<CommentDTO> getLatestComments() {
        return commentService.getLatestComments();
    }

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO dto, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        System.out.println(">>> Usuario autenticado: " + currentUser.getUsername());
        return ResponseEntity.ok(commentService.createComment(dto, currentUser.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long id,
                                                    @RequestBody CommentDTO dto,
                                                    Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(commentService.updateComment(id, dto, user.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id,
                                              Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        commentService.deleteComment(id, user.getId());
        return ResponseEntity.noContent().build();
    }

}
