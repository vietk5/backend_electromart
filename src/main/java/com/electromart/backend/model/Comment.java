package com.electromart.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private String username;

    @Column(columnDefinition = "TEXT")
    private String content;

    private int rating;

    private String imageUrl; 

    private LocalDateTime createdAt = LocalDateTime.now();
}
