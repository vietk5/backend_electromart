package com.electromart.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "spin_history",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "spin_date"})
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class SpinHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id", nullable = false)
    private Long userId;

    @Column(name="spin_date", nullable = false)
    private LocalDate spinDate; // để chặn 1 lần/ngày

    @Column(nullable = false)
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpinRewardType type;

    @Column(length = 255)
    private String value;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
