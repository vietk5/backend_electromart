package com.electromart.backend.repository;

import com.electromart.backend.model.KhachHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KhachHangRepository extends JpaRepository<KhachHang, Long> {

    @Query("""
        SELECT k FROM KhachHang k
        WHERE (:kw IS NULL OR :kw = '' 
           OR lower(k.hoTen) LIKE lower(concat('%', :kw, '%'))
           OR lower(k.email) LIKE lower(concat('%', :kw, '%'))
           OR k.soDienThoai LIKE concat('%', :kw, '%'))
        """)
    Page<KhachHang> searchByKeyword(@Param("kw") String keyword, Pageable pageable);
}
