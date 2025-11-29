package com.electromart.backend.model;

import com.electromart.backend.model.base.AuditEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "chi_tiet_don_hang")
public class ChiTietDonHang extends AuditEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "don_hang_id", nullable = false)
    private DonHang donHang;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "san_pham_id", nullable = false)
    private SanPham sanPham;

    @Column(name = "so_luong", nullable = false)
    private Integer soLuong;

    @Column(name = "don_gia", nullable = false, precision = 16, scale = 2)
    private BigDecimal donGia;

    @Column(name = "thanh_tien", nullable = false, precision = 16, scale = 2)
    private BigDecimal thanhTien;

    // Tự động tính thành tiền trước khi INSERT / UPDATE
    @PrePersist
    @PreUpdate
    private void calcThanhTien() {
        if (donGia != null && soLuong != null) {
            this.thanhTien = donGia.multiply(BigDecimal.valueOf(soLuong));
        }
    }
}
