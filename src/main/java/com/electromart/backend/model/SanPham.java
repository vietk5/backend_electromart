package com.electromart.backend.model;

// SanPham.java
import com.electromart.backend.model.base.AuditEntity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "san_pham")
public class SanPham extends AuditEntity {

    @Column(nullable = false, length = 200)
    private String ten;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "loai_id")
    private LoaiSanPham loai;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "thuong_hieu_id")
    private ThuongHieu thuongHieu;

    @Column(nullable = false, precision = 16, scale = 2)
    private BigDecimal gia;

    @Column(length = 500)
    private String moTaNgan;

    @Column(length = 2048)
    private String imageUrl;
}
