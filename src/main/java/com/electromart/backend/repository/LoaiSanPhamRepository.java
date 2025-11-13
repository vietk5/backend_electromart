// src/main/java/com/electromart/backend/repository/LoaiSanPhamRepository.java
package com.electromart.backend.repository;

import com.electromart.backend.model.LoaiSanPham;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoaiSanPhamRepository extends JpaRepository<LoaiSanPham, Long> {}
