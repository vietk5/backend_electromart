// DonHangRepository.java
package com.electromart.backend.repository;

import com.electromart.backend.model.DonHang;
import com.electromart.backend.model.TrangThaiDonHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DonHangRepository extends JpaRepository<DonHang, Long> {

    // Lấy đơn hàng mới nhất cho dashboard admin
    Page<DonHang> findAllByOrderByNgayDatHangDesc(Pageable pageable);

    // ✅ DÙNG ĐỂ TÍNH DOANH THU – FETCH LUÔN chiTiet
    @Query("""
           select distinct d
           from DonHang d
           left join fetch d.chiTiet ct
           where d.trangThai = :status
             and d.ngayDatHang between :from and :to
           """)
    List<DonHang> findRevenueOrders(
            @Param("status") TrangThaiDonHang status,
            @Param("from") LocalDateTime from,
            @Param("to")   LocalDateTime to
    );
    // them de lay don hang
    List<DonHang> findByKhachHangIdOrderByNgayDatHangDesc(Long khachHangId);

}
