package com.myvideogamelist.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.myvideogamelist.backend.dto.GameListDTO;
import com.myvideogamelist.backend.mapper.GameListMapper;
import com.myvideogamelist.backend.model.GameList;
import com.myvideogamelist.backend.model.ListType;
import com.myvideogamelist.backend.model.User;
import com.myvideogamelist.backend.repository.GameListRepository;
import com.myvideogamelist.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameListService {
	
	private final GameListRepository gameListRepository;
    private final UserRepository userRepository;

    public List<GameListDTO> getAllLists() {
        return gameListRepository.findAll().stream()
                .map(GameListMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<GameListDTO> getListById(Long id) {
        return gameListRepository.findById(id).map(GameListMapper::toDTO);
    }

    public GameListDTO createGameList(GameListDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        GameList list = new GameList();
        list.setUser(user);
        list.setName(ListType.valueOf(dto.getName().toUpperCase())); // por si llega en minúsculas

        GameList saved = gameListRepository.save(list);
        return GameListMapper.toDTO(saved);
    }

    public GameListDTO updateGameList(Long id, GameListDTO dto) {
        GameList existing = gameListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lista no encontrada"));

        if (dto.getName() != null) {
            existing.setName(ListType.valueOf(dto.getName().toUpperCase()));
        }

        return GameListMapper.toDTO(gameListRepository.save(existing));
    }

    public void deleteList(Long id) {
        gameListRepository.deleteById(id);
    }

    public List<GameListDTO> getListsByUserId(Long userId) {
        return gameListRepository.findByUserId(userId).stream()
                .map(GameListMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void createDefaultListsForUser(User user) {
        for (ListType type : ListType.values()) {
            GameList list = new GameList();
            list.setUser(user);
            list.setName(type);
            gameListRepository.save(list);
        }
    }

}
