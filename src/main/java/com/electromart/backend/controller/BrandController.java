package com.electromart.backend.controller;

import com.electromart.backend.dto.admin.BrandDto;
import com.electromart.backend.model.ThuongHieu;
import com.electromart.backend.repository.ThuongHieuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/brands") // Đường dẫn khớp với Android @GET("api/brands")
@CrossOrigin(origins = "*")      // Cho phép gọi từ mọi nguồn
public class BrandController {

    @Autowired
    private ThuongHieuRepository thuongHieuRepository;

    @GetMapping
    public ResponseEntity<List<BrandDto>> getAllBrands() {
        // 1. Lấy tất cả entity từ DB
        List<ThuongHieu> list = thuongHieuRepository.findAll();

        // 2. Chuyển đổi Entity sang DTO (chỉ lấy id và tên)
        List<BrandDto> dtoList = list.stream()
                .map(th -> new BrandDto(th.getId(), th.getTen()))
                .collect(Collectors.toList());

        // 3. Trả về cho Android
        return ResponseEntity.ok(dtoList);
    }
}