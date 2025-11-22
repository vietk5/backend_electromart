// DonHang.java
package com.electromart.backend.model;

import com.electromart.backend.model.TrangThaiDonHang;
import com.electromart.backend.model.base.AuditEntity;
import com.electromart.backend.model.KhachHang;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "don_hang")
public class DonHang extends AuditEntity {

    @Column(name = "ngay_dat_hang", nullable = false)
    private LocalDateTime ngayDatHang = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "khach_hang_id")
    private KhachHang khachHang;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", length = 30, nullable = false)
    private TrangThaiDonHang trangThai = TrangThaiDonHang.MOI;

    // ✅ ĐỔI THÀNH ENUM, KHÔNG PHẢI ManyToOne
    @Enumerated(EnumType.STRING)
    @Column(name = "phuong_thuc", length = 20, nullable = false)
    private PhuongThucThanhToan phuongThuc = PhuongThucThanhToan.COD;

    @OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChiTietDonHang> chiTiet = new ArrayList<>();

}
