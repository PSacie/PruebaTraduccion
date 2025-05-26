package com.myvideogamelist.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myvideogamelist.backend.model.GameList;

public interface GameListRepository extends JpaRepository<GameList, Long> {
	
	List<GameList> findByUserId(Long userId);

}
