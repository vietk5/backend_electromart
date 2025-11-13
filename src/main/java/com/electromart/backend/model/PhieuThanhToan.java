// PhieuThanhToan.java
package com.electromart.backend.model;

import com.electromart.backend.model.base.AuditEntity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "phieu_thanh_toan")
public class PhieuThanhToan extends AuditEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "khach_hang_id")
    private KhachHang khachHang;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PhuongThucThanhToan phuongThuc;

    @Column(nullable = false, precision = 16, scale = 2)
    private BigDecimal soTien;

    @Column(length = 50)
    private String trangThai; // SUCCESS/PENDING/FAILED...
}
