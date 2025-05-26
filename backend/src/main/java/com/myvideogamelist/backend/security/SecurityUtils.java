/*package com.myvideogamelist.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import com.myvideogamelist.backend.repository.UserRepository;

import com.myvideogamelist.backend.model.User;

@Component
public class SecurityUtils {
    
    private static UserRepository userRepository;

    @Autowired
    public SecurityUtils(UserRepository userRepository) {
        SecurityUtils.userRepository = userRepository;
    }

    public static User getAuthenticatedUserOrThrow401() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            System.out.println(">>> Usuario no autenticado");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Debes iniciar sesión");
        }

        // Verificamos si el principal es una instancia de UserDetails
        Object principal = authentication.getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        System.out.println(">>> Usuario autenticado: " + username);

        // Obtenemos el usuario directamente de la base de datos
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));
        
        System.out.println(">>> Usuario autenticado: " + user.getUsername() + " - Rol: " + user.getRole().getName());

        return user;
    }
}*/
