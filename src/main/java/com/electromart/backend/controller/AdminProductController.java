package com.electromart.backend.controller;

import com.electromart.backend.dto.admin.AdminProductDto;
import com.electromart.backend.mapper.ProductMapper;
import com.electromart.backend.model.LoaiSanPham;       // Import Model
import com.electromart.backend.model.SanPham;    // Import Model
import com.electromart.backend.model.ThuongHieu; // Import Model
import com.electromart.backend.repository.LoaiSanPhamRepository;       // Import Repo
import com.electromart.backend.repository.SanPhamRepository;
import com.electromart.backend.repository.ThuongHieuRepository; // Import Repo
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/products")
@CrossOrigin(origins = "*")
public class AdminProductController {

    private final SanPhamRepository sanPhamRepository;
    // Khai báo thêm 2 repo này để xử lý Loại và Thương hiệu
    private final LoaiSanPhamRepository loaiRepository;
    private final ThuongHieuRepository thuongHieuRepository;

    // Constructor Injection
    public AdminProductController(SanPhamRepository sanPhamRepository,
                                  LoaiSanPhamRepository loaiRepository,
                                  ThuongHieuRepository thuongHieuRepository) {
        this.sanPhamRepository = sanPhamRepository;
        this.loaiRepository = loaiRepository;
        this.thuongHieuRepository = thuongHieuRepository;
    }

    // 1. Đọc sản phẩm (Giữ nguyên)
    @GetMapping
    @Transactional(readOnly = true)
    public List<AdminProductDto> getProducts(
            @RequestParam(required = false) Long loaiId,
            @RequestParam(required = false) Long thuongHieuId,
            @RequestParam(required = false) String keyword
    ) {
        List<SanPham> list = sanPhamRepository.findAll();
        return list.stream()
                .filter(p -> loaiId == null || (p.getLoai() != null && loaiId.equals(p.getLoai().getId())))
                .filter(p -> thuongHieuId == null || (p.getThuongHieu() != null && thuongHieuId.equals(p.getThuongHieu().getId())))
                .filter(p -> {
                    if (keyword == null || keyword.isBlank()) return true;
                    String k = keyword.toLowerCase(Locale.ROOT);
                    return p.getTen() != null && p.getTen().toLowerCase(Locale.ROOT).contains(k);
                })
                .map(ProductMapper::toAdminDto)
                .collect(Collectors.toList());
    }

    // ============================================================
    // 2. API TẠO SẢN PHẨM (FULL LOGIC)
    // ============================================================
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody AdminProductDto dto) {
        try {
            SanPham sp = new SanPham();
            sp.setTen(dto.getTen());
            sp.setGia(dto.getGia());
            sp.setTonKho(dto.getTonKho());
            sp.setImageUrl(dto.getImageUrl());
            sp.setMoTaNgan(dto.getMoTaNgan());

            // --- XỬ LÝ LOẠI SẢN PHẨM ---
            if (dto.getLoaiId() != null) {
                // Nếu có ID -> Tìm theo ID
                LoaiSanPham loai = loaiRepository.findById(dto.getLoaiId())
                        .orElseThrow(() -> new RuntimeException("Loại ID không tồn tại"));
                sp.setLoai(loai);
            } else if (dto.getLoaiTen() != null && !dto.getLoaiTen().isBlank()) {
                // Nếu không có ID mà có Tên -> Tìm theo tên hoặc tạo mới
                String tenLoai = dto.getLoaiTen().trim();
                LoaiSanPham loai = loaiRepository.findByTen(tenLoai) // Đảm bảo repo có hàm findByTen
                        .orElseGet(() -> {
                            LoaiSanPham newLoai = new LoaiSanPham();
                            newLoai.setTen(tenLoai);
                            return loaiRepository.save(newLoai);
                        });
                sp.setLoai(loai);
            } else {
                return ResponseEntity.badRequest().body("Lỗi: Chưa chọn Loại sản phẩm");
            }

            // --- XỬ LÝ THƯƠNG HIỆU ---
            if (dto.getThuongHieuId() != null) {
                ThuongHieu th = thuongHieuRepository.findById(dto.getThuongHieuId())
                        .orElseThrow(() -> new RuntimeException("Thương hiệu ID không tồn tại"));
                sp.setThuongHieu(th);
            } else if (dto.getThuongHieuTen() != null && !dto.getThuongHieuTen().isBlank()) {
                String tenTH = dto.getThuongHieuTen().trim();
                ThuongHieu th = thuongHieuRepository.findByTen(tenTH) // Đảm bảo repo có hàm findByTen
                        .orElseGet(() -> {
                            ThuongHieu newTh = new ThuongHieu();
                            newTh.setTen(tenTH);
                            return thuongHieuRepository.save(newTh);
                        });
                sp.setThuongHieu(th);
            } else {
                return ResponseEntity.badRequest().body("Lỗi: Chưa chọn Thương hiệu");
            }

            SanPham saved = sanPhamRepository.save(sp);
            return ResponseEntity.ok(ProductMapper.toAdminDto(saved));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Lỗi tạo sản phẩm: " + e.getMessage());
        }
    }

    // 3. Cập nhật kho (Giữ nguyên)
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Void> changeStock(@PathVariable Long id, @RequestParam int delta) {
        SanPham sp = sanPhamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
        int current = sp.getTonKho() == null ? 0 : sp.getTonKho();
        sp.setTonKho(current + delta);
        sanPhamRepository.save(sp);
        return ResponseEntity.ok().build();
    }

    // 4. Xóa (Giữ nguyên)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!sanPhamRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        sanPhamRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}