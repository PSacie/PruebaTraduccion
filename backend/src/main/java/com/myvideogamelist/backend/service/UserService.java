/*package com.myvideogamelist.backend.service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.myvideogamelist.backend.dto.AdminUserDTO;
import com.myvideogamelist.backend.dto.PublicUserDTO;
import com.myvideogamelist.backend.dto.UserProfileDTO;
import com.myvideogamelist.backend.mapper.UserMapper;
import com.myvideogamelist.backend.model.Role;
import com.myvideogamelist.backend.model.User;
import com.myvideogamelist.backend.repository.RoleRepository;
import com.myvideogamelist.backend.repository.UserRepository;
import com.myvideogamelist.backend.security.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final RoleRepository roleRepository;

    public List<AdminUserDTO> getAllUsers() {
        return userRepository.findAll().stream()
            .map(UserMapper::toAdminDTO)
            .collect(Collectors.toList());
    }
    
    public UserProfileDTO getCurrentUserProfile() {
        User user = SecurityUtils.getAuthenticatedUserOrThrow401();
        return UserMapper.toUserProfileDTO(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<UserProfileDTO> searchUsers(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username).stream()
            .map(UserMapper::toUserProfileDTO)
            .collect(Collectors.toList());
    }
    
    public PublicUserDTO getPublicUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        return new PublicUserDTO(user.getId(), user.getUsername(), user.getAvatarUrl());
    }
    
    public UserProfileDTO getUserProfileById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return UserMapper.toUserProfileDTO(user);
    }

    public User createUser(User user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName("USER")
            .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));
        user.setRole(userRole);

        return userRepository.save(user);
    }

    public User updateUserProfile(Long id, String username, String email, String avatarUrl) {
        User currentUser = SecurityUtils.getAuthenticatedUserOrThrow401();
        User userInDB = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar si el usuario autenticado es el mismo que el que se va a editar
        if (!userInDB.getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("No tienes permiso para editar este perfil");
        }

        // Actualizar datos solo si se proporcionan
        if (username != null && !username.isEmpty()) {
            userInDB.setUsername(username);
        }

        if (email != null && !email.isEmpty()) {
            userInDB.setEmail(email);
        }

        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            userInDB.setAvatarUrl(avatarUrl);
        }

        return userRepository.save(userInDB);
    }

    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User currentUser = SecurityUtils.getAuthenticatedUserOrThrow401();

        if (!currentUser.getId().equals(userId)) {
            throw new AccessDeniedException("No puedes cambiar la contraseña de otro usuario");
        }

        if (!passwordEncoder.matches(currentPassword, currentUser.getPasswordHash())) {
            throw new AccessDeniedException("La contraseña actual no es correcta");
        }

        currentUser.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(currentUser);
    }

    public void deleteUser(Long id) {
        User currentUser = SecurityUtils.getAuthenticatedUserOrThrow401();

        boolean isSameUser = id.equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole().getName().equalsIgnoreCase("admin");

        if (!isSameUser && !isAdmin) {
            throw new AccessDeniedException("No tienes permiso para borrar esta cuenta.");
        }

        userRepository.deleteById(id);
    }

    public void recoverPassword(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("No existe un usuario con ese correo"));

        String tempPassword = generateRandomPassword(10);
        user.setPasswordHash(passwordEncoder.encode(tempPassword));
        userRepository.save(user);

        emailService.sendPasswordResetEmail(email, user.getUsername(), tempPassword);
    }

    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
    
    public void changeUserRole(Long targetUserId, String newRoleName) {
        User currentUser = SecurityUtils.getAuthenticatedUserOrThrow401();

        if (!currentUser.getRole().getName().equalsIgnoreCase("admin")) {
            throw new AccessDeniedException("Solo los administradores pueden cambiar roles.");
        }

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Usuario objetivo no encontrado"));

        Role newRole = roleRepository.findByName(newRoleName.toLowerCase())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + newRoleName));

        targetUser.setRole(newRole);
        userRepository.save(targetUser);
    }

}*/
