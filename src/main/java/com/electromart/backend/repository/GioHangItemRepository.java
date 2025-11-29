package com.electromart.backend.repository;


import com.electromart.backend.model.GioHangItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GioHangItemRepository extends JpaRepository<GioHangItem, Long> {

    // dùng cho xóa 1 sản phẩm khỏi giỏ
    void deleteByGioHangIdAndSanPhamId(Long gioHangId, Long sanPhamId);
}
