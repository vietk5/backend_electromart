package com.electromart.backend.controller;

import com.electromart.backend.model.SanPham;
import com.electromart.backend.repository.SanPhamRepository;
import com.electromart.backend.dto.ProductDto;  // Import the ProductDto from the dto package
import com.electromart.backend.mapper.ProductMapper;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final SanPhamRepository repo;

    public ProductController(SanPhamRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<ProductDto> products() {
        return repo.findAllWithJoins().stream()
                .map(sp -> new ProductDto(
                        sp.getId(),
                        sp.getTen(),
                        sp.getThuongHieu() != null ? sp.getThuongHieu().getTen() : null,
                        sp.getLoai() != null ? sp.getLoai().getTen() : null,
                        sp.getGia(),
                        sp.getImageUrl()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/category/{categoryId}")
    @Transactional
    public List<ProductDto> getProductsByCategory(@PathVariable Long categoryId) {
        List<SanPham> products = repo.findByLoaiId(categoryId);
        return products.stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());
    }
}
