package com.electromart.backend.model;

// ThuongHieu.java
import com.electromart.backend.model.base.AuditEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "thuong_hieu")
public class ThuongHieu extends AuditEntity {
    @Column(nullable = false, unique = true, length = 120)
    private String ten;
}
