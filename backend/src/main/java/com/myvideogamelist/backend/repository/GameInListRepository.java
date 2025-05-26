package com.myvideogamelist.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myvideogamelist.backend.model.GameInList;

public interface GameInListRepository extends JpaRepository<GameInList, Long> {
	
	List<GameInList> findByGameListId(Long gameListId);
	boolean existsByGameListIdAndGameId(Long gameListId, Long gameId);

}
