package com.electromart.backend.repository;

import com.electromart.backend.model.SpinReward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpinRewardRepository extends JpaRepository<SpinReward, Long> {
    List<SpinReward> findByActiveTrue();
}
