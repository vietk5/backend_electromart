package com.electromart.backend.controller;

import com.electromart.backend.dto.RatingSummary;
import com.electromart.backend.model.SanPham;
import com.electromart.backend.repository.CommentRepository;
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
    private final CommentRepository commentRepo;

    public ProductController(SanPhamRepository repo, CommentRepository commentRepo) {
        this.repo = repo;
        this.commentRepo = commentRepo;
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
                        sp.getImageUrl(),
                        sp.getMoTaNgan()
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
    // lấy sản phẩm theo id để hiển thị trên chi tiết sản phẩm
    @GetMapping("/{id}")
    @Transactional
    public ProductDto getProductById(@PathVariable Long id) {
        SanPham sp = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với id = " + id));

        return ProductMapper.toDto(sp);
    }
    // lấy các sản phảm có liên quan đến sản phẩm trong chi tiết sản phẩm đó
   @GetMapping("/{id}/related")
    public List<ProductDto> getRelated(@PathVariable Long id) {

        // Load product FULL để không bị Lazy
        SanPham sp = repo.findByIdFull(id).orElseThrow();

        List<SanPham> list =
                repo.findByLoaiId(sp.getLoai().getId());

        return list.stream()
                .filter(p -> !p.getId().equals(id))
                .map(ProductMapper::toDto)
                .limit(10)
                .toList();
    }
    @GetMapping("/{id}/rating-summary")
    public RatingSummary getRatingSummary(@PathVariable Long id) {
        Double avgObj = commentRepo.averageRating(id);
        double avg = avgObj != null ? avgObj : 0.0;

        int c1 = commentRepo.countRating(id, 1);
        int c2 = commentRepo.countRating(id, 2);
        int c3 = commentRepo.countRating(id, 3);
        int c4 = commentRepo.countRating(id, 4);
        int c5 = commentRepo.countRating(id, 5);

        return new RatingSummary(avg, c1, c2, c3, c4, c5);
    }


}
