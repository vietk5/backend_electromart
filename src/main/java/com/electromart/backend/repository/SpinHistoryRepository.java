package com.electromart.backend.repository;

import com.electromart.backend.model.SpinHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface SpinHistoryRepository extends JpaRepository<SpinHistory, Long> {
    Optional<SpinHistory> findByUserIdAndSpinDate(Long userId, LocalDate spinDate);
    long countByUserIdAndSpinDate(Long userId, LocalDate spinDate);
}
