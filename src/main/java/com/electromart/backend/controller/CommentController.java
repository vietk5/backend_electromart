package com.electromart.backend.controller;

import com.electromart.backend.dto.CommentDto;
import com.electromart.backend.model.Comment;
import com.electromart.backend.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository repo;

    // Lấy comment theo productId
    @GetMapping("/{productId}")
    public List<CommentDto> getComments(@PathVariable Long productId) {
        return repo.findByProductIdOrderByCreatedAtDesc(productId)
                .stream()
                .map(c -> new CommentDto(
                        c.getId(),
                        c.getProductId(),
                        c.getUsername(),
                        c.getContent(),
                        c.getRating(),
                        c.getCreatedAt()
                ))
                .toList();
    }

    // Gửi bình luận
    @PostMapping
    public CommentDto addComment(@RequestBody CommentDto dto) {
        Comment c = new Comment();
        c.setProductId(dto.productId());
        c.setUsername(dto.username());
        c.setContent(dto.content());
        c.setRating(dto.rating());
        repo.save(c);

        return new CommentDto(
                c.getId(),
                c.getProductId(),
                c.getUsername(),
                c.getContent(),
                c.getRating(),
                c.getCreatedAt()
        );
    }
}
