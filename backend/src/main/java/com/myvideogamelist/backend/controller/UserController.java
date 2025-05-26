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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myvideogamelist.backend.dto.AdminUserDTO;
import com.myvideogamelist.backend.dto.ChangePasswordRequest;
import com.myvideogamelist.backend.dto.ChangeRoleRequest;
import com.myvideogamelist.backend.dto.PublicUserDTO;
import com.myvideogamelist.backend.dto.UserProfileDTO;
import com.myvideogamelist.backend.mapper.UserMapper;
import com.myvideogamelist.backend.model.User;
import com.myvideogamelist.backend.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // Permite acceso desde frontend
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;

	@GetMapping
	public List<AdminUserDTO> getAllUsers() {
	    return userService.getAllUsers();
	}
    
    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUserProfile());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDTO> getUserById(@PathVariable Long id) {
        UserProfileDTO userProfile = userService.getUserProfileById(id);
        return ResponseEntity.ok(userProfile);
    }

    @GetMapping("/search")
    public List<UserProfileDTO> searchUsers(@RequestParam String username) {
        return userService.searchUsers(username);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfileDTO> updateUserProfile(
            @PathVariable Long id,
            @RequestBody UserProfileDTO updatedUser) {
        User updated = userService.updateUserProfile(id, updatedUser.getUsername(), updatedUser.getEmail(), updatedUser.getAvatarUrl());
        return ResponseEntity.ok(UserMapper.toUserProfileDTO(updated));
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<String> changePassword(@PathVariable Long id, @RequestBody ChangePasswordRequest request) {
        userService.changePassword(id, request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/recover-password")
    public ResponseEntity<String> recoverPassword(@RequestParam String email) {
        userService.recoverPassword(email);
        return ResponseEntity.ok("Se ha enviado una nueva contraseña a tu correo.");
    }
    
    @PutMapping("/{id}/role")
    public ResponseEntity<String> changeUserRole(@PathVariable Long id,
                                                 @RequestBody ChangeRoleRequest request) {
        userService.changeUserRole(id, request.getRoleName());
        return ResponseEntity.ok("Rol actualizado correctamente");
    }

}
