// PhieuGiamGia.java
package com.electromart.backend.model;

import com.electromart.backend.model.base.AuditEntity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "phieu_giam_gia")
public class PhieuGiamGia extends AuditEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(precision = 16, scale = 2)
    private BigDecimal giamToiDa;

    private Integer phanTram;      // nếu dùng % giảm
    private Instant hieuLucTu;
    private Instant hieuLucDen;

    private boolean hoatDong = true;
}
