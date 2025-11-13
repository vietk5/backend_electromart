// NguoiDung.java
package com.electromart.backend.model;

import com.electromart.backend.model.base.AuditEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "nguoi_dung")
@Inheritance(strategy = InheritanceType.JOINED)
public class NguoiDung extends AuditEntity {
    @Column(nullable = false, unique = true, length = 120)
    private String email;
    @Column(nullable = false, length = 120)
    private String matKhau;
    @Column(length = 120)
    private String hoTen;
    @Column(length = 20)
    private String soDienThoai;
}
