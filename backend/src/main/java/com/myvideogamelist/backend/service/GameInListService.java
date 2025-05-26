/*package com.myvideogamelist.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.myvideogamelist.backend.dto.GameInListDTO;
import com.myvideogamelist.backend.mapper.GameInListMapper;
import com.myvideogamelist.backend.model.Game;
import com.myvideogamelist.backend.model.GameInList;
import com.myvideogamelist.backend.model.GameList;
import com.myvideogamelist.backend.model.User;
import com.myvideogamelist.backend.repository.GameInListRepository;
import com.myvideogamelist.backend.repository.GameListRepository;
import com.myvideogamelist.backend.repository.GameRepository;
import com.myvideogamelist.backend.security.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameInListService {

	private final GameInListRepository gameInListRepository;
    private final GameListRepository gameListRepository;
    private final GameRepository gameRepository;

    public GameInListDTO addGameToList(Long listId, Long gameId) {
        GameList list = gameListRepository.findById(listId)
                .orElseThrow(() -> new RuntimeException("Lista no encontrada"));
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado"));

        User currentUser = SecurityUtils.getAuthenticatedUserOrThrow401();
        System.out.println(">>> Usuario autenticado: " + currentUser.getUsername());
        
        System.out.println(">>> list.getUser().getId(): " + list.getUser().getId());
        System.out.println(">>> currentUser.getId(): " + currentUser.getId());

        if (!list.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("No puedes añadir juegos a listas de otros usuarios.");
        }

        if (gameInListRepository.existsByGameListIdAndGameId(listId, gameId)) {
            throw new IllegalArgumentException("Este juego ya está en la lista.");
        }
        
        deleteIfExistsInOtherList(currentUser.getId(), gameId);

        GameInList entity = new GameInList();
        entity.setGameList(list);
        entity.setGame(game);

        return GameInListMapper.toDTO(gameInListRepository.save(entity));
    }

    public void removeGameFromList(Long id) {
        GameInList gameInList = gameInListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Elemento no encontrado"));

        GameList list = gameInList.getGameList();
        User currentUser = SecurityUtils.getAuthenticatedUserOrThrow401();

        if (!list.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("No puedes eliminar juegos de listas que no son tuyas.");
        }

        gameInListRepository.deleteById(id);
    }

    public List<GameInListDTO> getGamesByListId(Long listId) {
        return gameInListRepository.findByGameListId(listId).stream()
                .map(GameInListMapper::toDTO)
                .collect(Collectors.toList());
    }

    public boolean existsByListAndGame(Long listId, Long gameId) {
        return gameInListRepository.existsByGameListIdAndGameId(listId, gameId);
    }
    
    public void deleteIfExistsInOtherList(Long userId, Long gameId) {
        List<GameList> userLists = gameListRepository.findByUserId(userId);

        for (GameList list : userLists) {
            List<GameInList> entries = gameInListRepository.findByGameListId(list.getId());

            for (GameInList entry : entries) {
                if (entry.getGame().getId().equals(gameId)) {
                    gameInListRepository.delete(entry);
                    return; // Solo puede estar en una, así que lo eliminamos y salimos
                }
            }
        }
    }
}*/
