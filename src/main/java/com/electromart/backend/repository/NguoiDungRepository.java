package com.electromart.backend.repository;

import com.electromart.backend.model.NguoiDung;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NguoiDungRepository extends JpaRepository<NguoiDung, Long> {
    boolean existsByEmail(String email);
    Optional<NguoiDung> findByEmail(String email);
}
