package com.electromart.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "ten_nguoi_nhan", nullable = false)
    private String tenNguoiNhan;
    
    @Column(name = "so_dien_thoai", nullable = false)
    private String soDienThoai;
    
    @Column(name = "tinh_thanh_pho")
    private String tinhThanhPho;
    
    @Column(name = "quan_huyen")
    private String quanHuyen;
    
    @Column(name = "phuong_xa")
    private String phuongXa;
    
    @Column(name = "dia_chi_chi_tiet", columnDefinition = "TEXT")
    private String diaChiChiTiet;
    
    @Column(name = "loai_dia_chi")
    private String loaiDiaChi; // "Nhà" hoặc "Văn phòng"
    
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relationship với User (nếu cần)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private NguoiDung user;
}
