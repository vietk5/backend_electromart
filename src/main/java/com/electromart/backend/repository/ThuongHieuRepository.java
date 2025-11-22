package com.electromart.backend.repository;

import com.electromart.backend.model.ThuongHieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThuongHieuRepository extends JpaRepository<ThuongHieu, Long> {
    // JpaRepository đã có sẵn hàm findAll() nên không cần viết thêm gì
    Optional<ThuongHieu> findByTen(String ten);
}