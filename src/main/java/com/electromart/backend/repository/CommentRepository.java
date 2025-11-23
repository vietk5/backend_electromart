package com.electromart.backend.repository;

import com.electromart.backend.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByProductIdOrderByCreatedAtDesc(Long productId);
}
