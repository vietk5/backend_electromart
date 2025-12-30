package com.electromart.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "spin_rewards")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class SpinReward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Text hiển thị trên vòng quay (PHẢI trùng với label Android để map)
    @Column(nullable = false, unique = true)
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpinRewardType type;

    // nếu type=VOUCHER => value = voucherCode
    // nếu MESSAGE => value = message
    // nếu GIFT => value = tên quà (ví dụ: "Stickers")
    @Column(length = 255)
    private String value;

    // trọng số random: càng lớn càng dễ trúng
    @Column(nullable = false)
    private int weight;

    @Column(nullable = false)
    private boolean active = true;
}
