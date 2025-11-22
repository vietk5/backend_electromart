// src/main/java/com/electromart/backend/repository/SanPhamRepository.java
package com.electromart.backend.repository;

import com.electromart.backend.model.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SanPhamRepository extends JpaRepository<SanPham, Long> {
    // Lấy kèm Loại & Thương hiệu để tránh LazyInitializationException
    @Query("""
           select sp from SanPham sp
           join fetch sp.loai
           join fetch sp.thuongHieu
           """)
    List<SanPham> findAllWithJoins();

    // Lấy sản phẩm theo LoaiSanPham (danh mục)
    @Query("SELECT sp FROM SanPham sp JOIN FETCH sp.thuongHieu WHERE sp.loai.id = :categoryId")
    List<SanPham> findByLoaiId(@Param("categoryId") Long categoryId);

    @Query("SELECT COALESCE(SUM(s.tonKho), 0) FROM SanPham s")
    long sumAllStock();

    @Query("SELECT s FROM SanPham s ORDER BY s.tonKho ASC")
    Page<SanPham> findLowStock(Pageable pageable);
}
