package com.myvideogamelist.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
	
	private final JavaMailSender mailSender;

    public void sendPasswordResetEmail(String toEmail, String username, String tempPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Recuperación de contraseña - MyVideogameList");
        message.setText("Hola " + username + ",\n\n" +
                "Tu nueva contraseña temporal es: " + tempPassword + "\n\n" +
                "Por favor, inicia sesión y cámbiala desde tu perfil.");

        mailSender.send(message);
    }

}
