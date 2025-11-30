package com.electromart.backend.repository;


import com.electromart.backend.model.ChiTietDonHang;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChiTietDonHangRepository extends JpaRepository<ChiTietDonHang, Long> {

    // Lấy tất cả dòng chi tiết của 1 đơn
    List<ChiTietDonHang> findByDonHangId(Long donHangId);
}