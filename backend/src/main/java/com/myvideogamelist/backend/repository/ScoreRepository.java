package com.myvideogamelist.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.myvideogamelist.backend.dto.TopGameDTO;
import com.myvideogamelist.backend.model.Score;

public interface ScoreRepository extends JpaRepository<Score, Long> {
	
	@Query("SELECT AVG(s.score) FROM Score s WHERE s.game.id = :gameId")
	Double findAverageScoreByGameId(@Param("gameId") Long gameId);
	List<Score> findByUserId(Long userId);
	@Query("SELECT s FROM Score s WHERE s.user.id = :userId ORDER BY s.createdAt DESC")
	List<Score> findTop3LatestScoresByUser(@Param("userId") Long userId);
	Optional<Score> findByUserIdAndGameId(Long userId, Long gameId);
	@Query("SELECT g.id, g.title, g.coverImage, AVG(s.score) " +
		   "FROM Score s JOIN s.game g " +
		   "GROUP BY g.id, g.title, g.coverImage " +
		   "ORDER BY AVG(s.score) DESC")
	List<Object[]> findAverageScoresForAllGames();
	@Query("SELECT new com.myvideogamelist.backend.dto.TopGameDTO(" +
		       "g.id, g.title, g.genre, g.platform, g.coverImage, AVG(s.score)) " +
		       "FROM Score s JOIN s.game g " +
		       "WHERE (:genre IS NULL OR g.genre = :genre) AND (:platform IS NULL OR g.platform = :platform) " +
		       "GROUP BY g.id, g.title, g.genre, g.platform, g.coverImage " +
		       "ORDER BY AVG(s.score) DESC")
	List<TopGameDTO> findTopRatedGamesWithFilters(@Param("genre") String genre, @Param("platform") String platform);

}
