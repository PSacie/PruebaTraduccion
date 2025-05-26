package com.myvideogamelist.backend.service;

import com.myvideogamelist.backend.dto.AuthRequest;
import com.myvideogamelist.backend.dto.AuthResponse;
import com.myvideogamelist.backend.model.Role;
import com.myvideogamelist.backend.model.User;
import com.myvideogamelist.backend.repository.RoleRepository;
import com.myvideogamelist.backend.repository.UserRepository;
import com.myvideogamelist.backend.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final GameListService gameListService;

    public AuthResponse register(AuthRequest request) {
        Role userRole = roleRepository.findByName("user")
                .orElseThrow(() -> new RuntimeException("Role 'user' not found"));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail()); // puedes cambiarlo
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(userRole);

        userRepository.save(user);
        
        gameListService.createDefaultListsForUser(user);
        
        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token, user.getId(), user.getUsername(), user.getRole().getName());
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token, user.getId(), user.getUsername(), user.getRole().getName());
    }

}
