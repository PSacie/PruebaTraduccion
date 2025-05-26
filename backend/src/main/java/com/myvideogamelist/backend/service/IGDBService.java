package com.myvideogamelist.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myvideogamelist.backend.model.Game;
import com.myvideogamelist.backend.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.myvideogamelist.backend.model.User;
import com.myvideogamelist.backend.security.SecurityUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IGDBService {

    private final RestTemplate restTemplate;
    private final GameRepository gameRepository;

    @Value("${igdb.client.id}")
    private String clientId;

    @Value("${igdb.client.secret}")
    private String clientSecret;

    @Value("${igdb.base.url}")
    private String baseUrl;

    private String accessToken;

    @Autowired
    public IGDBService(RestTemplate restTemplate, GameRepository gameRepository) {
        this.restTemplate = restTemplate;
        this.gameRepository = gameRepository;
    }

    // Método para autenticar en IGDB
    private void authenticate() {
        try {
            String url = "https://id.twitch.tv/oauth2/token";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/x-www-form-urlencoded");

            String body = "client_id=" + clientId +
                    "&client_secret=" + clientSecret +
                    "&grant_type=client_credentials";

            HttpEntity<String> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                this.accessToken = extractAccessToken(response.getBody());
                System.out.println("Token obtenido correctamente.");
            } else {
                System.out.println("Error al autenticar con IGDB: " + response.getStatusCode());
                this.accessToken = null;
            }
        } catch (Exception e) {
            System.out.println("Error al autenticar en IGDB: " + e.getMessage());
            this.accessToken = null;
        }
    }

    // Método para extraer el token
    private String extractAccessToken(String responseBody) {
        try {
            String token = responseBody.split("\"access_token\":\"")[1].split("\"")[0];
            return token;
        } catch (Exception e) {
            System.out.println("Error al extraer el token: " + e.getMessage());
            return null;
        }
    }

    // Método para extraer la fecha
    private String extractReleaseDate(JsonNode gameNode) {
        if (gameNode.has("release_dates") && gameNode.get("release_dates").size() > 0) {
            JsonNode dateNode = gameNode.get("release_dates").get(0);
            if (dateNode.has("date")) {
                long timestamp = dateNode.get("date").asLong();
                return LocalDate.ofEpochDay(timestamp / 86400L).toString(); // Convertir a LocalDate
            }
        }
        return null;
    }

    // Método para obtener y guardar juegos
    public String fetchAndSaveGames(String query) {
    	
    	// Verificar si el usuario autenticado es admin
    	User currentUser = SecurityUtils.getAuthenticatedUserOrThrow401();
    	if (!currentUser.getRole().getName().equalsIgnoreCase("ADMIN")) {
    	    throw new SecurityException("Solo los administradores pueden importar juegos.");
    	}
    	
        if (accessToken == null) {
            authenticate();
        }

        if (accessToken == null) {
            return "Error: No se pudo autenticar en IGDB.";
        }

        try {
            String url = baseUrl + "games";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Client-ID", clientId);
            headers.set("Authorization", "Bearer " + accessToken);
            headers.set("Accept", "application/json");

            int batchSize = 100;
            int offset = 0;
            int importedCount = 0;
            int emptyBatchCount = 0;
            boolean hasMore = true;

            // Obtener todos los títulos existentes de una vez (mejora de rendimiento)
            List<String> existingTitles = gameRepository.findAll()
                    .stream()
                    .map(Game::getTitle)
                    .collect(Collectors.toList());

            while (hasMore) {
                String paginatedQuery = query + "; limit " + batchSize + "; offset " + offset + ";";
                System.out.println("Query Enviado a IGDB: " + paginatedQuery);

                HttpEntity<String> entity = new HttpEntity<>(paginatedQuery, headers);
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

                if (!response.getStatusCode().is2xxSuccessful()) {
                    System.out.println("Error en la respuesta de IGDB: " + response.getStatusCode());
                    return "Error: La API de IGDB respondió con un error. Código: " + response.getStatusCode();
                }

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonResponse = objectMapper.readTree(response.getBody());

                // Si la respuesta está vacía, detener el bucle
                if (jsonResponse.isEmpty()) {
                    System.out.println("No hay más juegos para importar.");
                    break;
                }

                int batchImportedCount = 0;

                for (JsonNode gameNode : jsonResponse) {
                    try {
                        String title = gameNode.get("name").asText();
                        
                        // Verificar en memoria si el juego ya existe (rápido)
                        if (existingTitles.contains(title)) {
                            System.out.println("Juego ya existente: " + title);
                            continue;
                        }

                        // Si es un juego nuevo, lo importamos
                        Game game = new Game();
                        game.setTitle(title);
                        game.setGenre(extractGenres(gameNode));
                        game.setReleaseDate(parseReleaseDate(gameNode));
                        game.setPlatform(extractPlatforms(gameNode));
                        game.setDescription(extractDescription(gameNode));
                        game.setCoverImage(extractCoverImage(gameNode));

                        gameRepository.save(game);
                        existingTitles.add(title); // Agregar el nuevo título a la lista
                        System.out.println("Juego guardado: " + game.getTitle());
                        batchImportedCount++;
                    } catch (Exception e) {
                        System.out.println("Error al guardar el juego: " + e.getMessage());
                    }
                }

                // Verificar si el lote estaba vacío
                if (batchImportedCount == 0) {
                    emptyBatchCount++;
                    System.out.println("Lote vacío. Intentos consecutivos sin nuevos juegos: " + emptyBatchCount);
                } else {
                    emptyBatchCount = 0;
                }

                // Si hemos tenido 3 lotes vacíos consecutivos, terminamos
                if (emptyBatchCount >= 3) {
                    System.out.println("Tres lotes consecutivos sin nuevos juegos. Finalizando.");
                    break;
                }

                importedCount += batchImportedCount;
                offset += batchSize;
            }

            System.out.println("Importación completada. Juegos importados: " + importedCount);
            return "Juegos importados correctamente. Total: " + importedCount;
        } catch (Exception e) {
            System.out.println("Error al importar juegos: " + e.getMessage());
            return "Error al importar juegos: " + e.getMessage();
        }
    }

    // Extraer plataformas
    private String extractPlatforms(JsonNode gameNode) {
        if (gameNode.has("platforms")) {
            StringBuilder platforms = new StringBuilder();
            for (JsonNode platform : gameNode.get("platforms")) {
                if (platforms.length() > 0) platforms.append(", ");
                platforms.append(platform.get("name").asText());
            }
            return platforms.toString();
        }
        return "Sin plataforma";
    }

    // Extraer descripción
    private String extractDescription(JsonNode gameNode) {
        if (gameNode.has("summary")) {
            return gameNode.get("summary").asText();
        }
        return "Sin descripción";
    }

    // Extraer imagen de portada
    private String extractCoverImage(JsonNode gameNode) {
        if (gameNode.has("cover")) {
            return "https://images.igdb.com/igdb/image/upload/t_cover_big/" + gameNode.get("cover").get("image_id").asText() + ".jpg";
        }
        return "https://via.placeholder.com/150";
    }

    // Método para extraer géneros
    private String extractGenres(JsonNode gameNode) {
        if (gameNode.has("genres")) {
            StringBuilder genres = new StringBuilder();
            for (JsonNode genre : gameNode.get("genres")) {
                if (genres.length() > 0) genres.append(", ");
                genres.append(genre.get("name").asText());
            }
            return genres.toString();
        }
        return "Sin género";
    }

    // Convertir la fecha de lanzamiento
    private LocalDate parseReleaseDate(JsonNode gameNode) {
        if (gameNode.has("release_dates") && gameNode.get("release_dates").size() > 0) {
            JsonNode dateNode = gameNode.get("release_dates").get(0);
            if (dateNode.has("date")) {
                long timestamp = dateNode.get("date").asLong();
                return LocalDate.ofEpochDay(timestamp / 86400L);
            }
        }
        return null;
    }
}
