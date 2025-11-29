package com.electromart.backend.model;

import com.electromart.backend.model.base.AuditEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "gio_hang_item")
public class GioHangItem extends AuditEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "gio_hang_id", nullable = false)
    private GioHang gioHang;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "san_pham_id", nullable = false)
    private SanPham sanPham;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "khach_hang_id", nullable = false)
    private KhachHang khachHang;

    @Column(name = "so_luong", nullable = false)
    private int soLuong;

    // CHỈ field này map tới cột "don_gia"
    @Column(name = "don_gia", nullable = false, precision = 16, scale = 2)
    private BigDecimal donGia;

    @Transient
    public BigDecimal getThanhTien() {
        if (donGia == null) return BigDecimal.ZERO;
        return donGia.multiply(BigDecimal.valueOf(soLuong));
    }
}
