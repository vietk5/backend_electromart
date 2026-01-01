package com.electromart.backend.dto;

import java.time.LocalDateTime;

public record CommentDto(
        Long id,
        Long productId,
        String username,
        String content,
        int rating,
        LocalDateTime createdAt,
        String imageUrl
) {}
