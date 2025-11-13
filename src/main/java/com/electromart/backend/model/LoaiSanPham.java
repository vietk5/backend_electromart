// LoaiSanPham.java
package com.electromart.backend.model;

import com.electromart.backend.model.base.AuditEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "loai_san_pham")
public class LoaiSanPham extends AuditEntity {
    @Column(nullable = false, unique = true, length = 120)
    private String ten;
}
