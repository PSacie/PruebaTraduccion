package com.myvideogamelist.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.myvideogamelist.backend.dto.TranslateResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TranslationService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String TRANSLATE_API_URL = "https://libretranslate.de/translate";

    public TranslateResponseDTO translateToSpanish(String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("q", text);
        body.put("source", "en");
        body.put("target", "es");
        body.put("format", "text");
        body.put("api_key", "");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<JsonNode> response = restTemplate.exchange(
                    TRANSLATE_API_URL,
                    HttpMethod.POST,
                    entity,
                    JsonNode.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            	System.out.println(">>> RESPUESTA COMPLETA: " + response.getBody());
            	String translatedText = response.getBody().get("translatedText").asText();
            	System.out.println(">>> TRADUCCIÓN REAL: " + translatedText);
                return new TranslateResponseDTO(translatedText);
            } else {
                throw new RuntimeException("La API de traducción devolvió una respuesta no válida.");
            }

        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RuntimeException("Error real: " + e.getClass().getName() + " - " + e.getMessage());
        }
    }
}