// src/main/java/com/electromart/backend/repository/LoaiSanPhamRepository.java
package com.electromart.backend.repository;

import com.electromart.backend.model.LoaiSanPham;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoaiSanPhamRepository extends JpaRepository<LoaiSanPham, Long> {
    Optional<LoaiSanPham> findByTen(String ten);
}
