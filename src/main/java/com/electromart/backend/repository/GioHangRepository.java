package com.electromart.backend.repository;

import com.electromart.backend.model.GioHang;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GioHangRepository extends JpaRepository<GioHang, Long> {

    Optional<GioHang> findByKhachHangId(Long khachHangId);
}
