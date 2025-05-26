package com.myvideogamelist.backend.service;

import com.myvideogamelist.backend.dto.TranslateResponseDTO;
import com.myvideogamelist.backend.repository.GameRepository;
import com.myvideogamelist.backend.repository.ScoreRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TranslationService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String TRANSLATE_API_URL = "https://translate.argosopentech.com/translate";

    public TranslateResponseDTO translateToSpanish(String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("q", text);
        body.put("source", "en");
        body.put("target", "es");
        body.put("format", "text");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    TRANSLATE_API_URL,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String translatedText = (String) response.getBody().get("translatedText");
                return new TranslateResponseDTO(translatedText);
            } else {
                throw new RuntimeException("La API de traducción devolvió una respuesta no válida.");
            }

        } catch (Exception e) {
        	System.err.println("API no disponible, devolviendo traducción simulada");
        	return new TranslateResponseDTO("[TRADUCIDO] " + text);
        }
    }
}