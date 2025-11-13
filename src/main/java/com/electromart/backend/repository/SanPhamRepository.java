// src/main/java/com/electromart/backend/repository/SanPhamRepository.java
package com.electromart.backend.repository;

import com.electromart.backend.model.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SanPhamRepository extends JpaRepository<SanPham, Long> {
    // Lấy kèm Loại & Thương hiệu để tránh LazyInitializationException
    @Query("""
           select sp from SanPham sp
           join fetch sp.loai
           join fetch sp.thuongHieu
           """)
    List<SanPham> findAllWithJoins();
}
