package com.electromart.backend.repository;

import com.electromart.backend.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByProductIdOrderByCreatedAtDesc(Long productId);
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.productId = :productId AND c.rating = :rating")
    int countRating(@Param("productId") Long productId, @Param("rating") int rating);

    @Query("SELECT AVG(c.rating) FROM Comment c WHERE c.productId = :productId")
    Double averageRating(@Param("productId") Long productId);

}
