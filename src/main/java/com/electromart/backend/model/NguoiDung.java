// NguoiDung.java
package com.electromart.backend.model;

import com.electromart.backend.model.base.AuditEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "nguoi_dung")
@Inheritance(strategy = InheritanceType.JOINED)
public class NguoiDung extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 120)
    private String email;
    @Column(nullable = false, length = 120)
    private String matKhau;
    @Column(length = 120)
    private String hoTen;
    @Column(length = 20)
    private String soDienThoai;
    @Column
    private String otp;
    @Column
    private LocalDateTime otpExpiry;
    
    public void setEmail(String email) {
        this.email = email;
    }
    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }
    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }
    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }
}
