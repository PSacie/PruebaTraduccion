/*package com.myvideogamelist.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.myvideogamelist.backend.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	
	List<Comment> findByGameId(Long gameId);
	List<Comment> findByUserId(Long userId);
	List<Comment> findTop3ByUserIdOrderByCommentDateDesc(Long userId);
	Optional<Comment> findByUserIdAndGameId(Long userId, Long gameId);
	@Query("SELECT c FROM Comment c ORDER BY c.commentDate DESC")
	List<Comment> findLatestComments(Pageable pageable);

}*/
