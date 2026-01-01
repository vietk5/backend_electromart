package com.electromart.backend.controller;

import com.electromart.backend.dto.CommentDto;
import com.electromart.backend.model.Comment;
import com.electromart.backend.repository.CommentRepository;
import com.electromart.backend.service.FileStorageService; // <-- Import service
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity; // <-- Import
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; // <-- Import
import org.springframework.web.servlet.support.ServletUriComponentsBuilder; // <-- Import

import java.util.List;
import java.util.Map; // <-- Import

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository repo;
    private final FileStorageService fileStorageService; // <-- Inject service

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
                        c.getCreatedAt(),
                        c.getImageUrl() // <-- Thêm imageUrl
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
        c.setImageUrl(dto.imageUrl()); // <-- Lưu imageUrl
        repo.save(c);

        return new CommentDto(
                c.getId(),
                c.getProductId(),
                c.getUsername(),
                c.getContent(),
                c.getRating(),
                c.getCreatedAt(),
                c.getImageUrl() // <-- Trả về imageUrl
        );
    }

    // Endpoint mới để upload ảnh
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.save(file);

        // Tạo URL để client có thể truy cập
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(fileName)
                .toUriString();

        // Trả về URL của ảnh trong một JSON object
        return ResponseEntity.ok(Map.of("url", fileDownloadUri));
    }
}
