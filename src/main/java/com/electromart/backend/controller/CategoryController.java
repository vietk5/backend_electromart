// src/main/java/com/electromart/backend/controller/CategoryController.java
package com.electromart.backend.controller;

import com.electromart.backend.model.LoaiSanPham;
import com.electromart.backend.repository.LoaiSanPhamRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final LoaiSanPhamRepository repo;

    public CategoryController(LoaiSanPhamRepository repo) {
        this.repo = repo;
    }

    // DTO đơn giản trả về cho app
    public record CategoryDto(Long id, String name) {}

    @GetMapping
    public List<CategoryDto> all() {
        return repo.findAll().stream()
                .map(l -> new CategoryDto(l.getId(), l.getTen()))
                .toList();
    }
}
