package com.electromart.backend.repository;

import com.electromart.backend.model.ThuongHieu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<ThuongHieu, Long> { }
