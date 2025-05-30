package com.myvideogamelist.backend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameListDTO {
    private Long id;
    private String name; // Jugados, Jugando, Pendientes
    private Long userId;
    private List<GameInListDTO> games;
}
