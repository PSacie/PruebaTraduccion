/*package com.myvideogamelist.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.myvideogamelist.backend.model.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
	
	List<Game> findByTitleContainingIgnoreCase(String title);
	Optional<Game> findByTitle(String title);
	boolean existsByTitle(String title);
	@Query(value = "SELECT * FROM games ORDER BY RAND() LIMIT 3", nativeQuery = true)
	List<Game> findRandomGames();
	@Query("SELECT g FROM Game g WHERE g.id NOT IN (SELECT DISTINCT s.game.id FROM Score s) " +
		       "AND (:genre IS NULL OR g.genre = :genre) AND (:platform IS NULL OR g.platform = :platform)")
	List<Game> findGamesWithoutScoresWithFilters(@Param("genre") String genre, @Param("platform") String platform);
	@Query("SELECT g FROM Game g WHERE g.releaseDate > CURRENT_DATE " +
		       "AND (:genre IS NULL OR g.genre = :genre) " +
		       "AND (:platform IS NULL OR g.platform = :platform) " +
		       "ORDER BY g.releaseDate ASC")
	List<Game> findUpcomingGamesWithFilters(
		    @Param("genre") String genre,
		    @Param("platform") String platform
			);
	@Query(value = "SELECT * FROM games " +
            "WHERE (:genre IS NULL OR genre = :genre) " +
            "AND (:platform IS NULL OR platform = :platform) " +
            "ORDER BY RAND() LIMIT 10", nativeQuery = true)
	List<Game> findRandomGamesWithFilters(
			@Param("genre") String genre,
			@Param("platform") String platform
			);

}*/
