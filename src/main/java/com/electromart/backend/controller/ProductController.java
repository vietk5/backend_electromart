// src/main/java/com/electromart/backend/controller/ProductController.java
package com.electromart.backend.controller;

import com.electromart.backend.repository.SanPhamRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final SanPhamRepository repo;
    public ProductController(SanPhamRepository repo) { this.repo = repo; }

    public record ProductDto(Long id, String name, String brand, java.math.BigDecimal price, String imageUrl) {}

    @GetMapping
    public List<ProductDto> products() {
        return repo.findAllWithJoins().stream()
                .map(sp -> new ProductDto(
                        sp.getId(),
                        sp.getTen(),
                        sp.getThuongHieu() != null ? sp.getThuongHieu().getTen() : null,
                        sp.getGia(),
                        sp.getImageUrl()
                ))
                .toList();
    }
}
