package com.electromart.backend.model;

import com.electromart.backend.model.base.AuditEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "gio_hang")
public class GioHang extends AuditEntity {
    
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "khach_hang_id")
    private KhachHang khachHang;

    @OneToMany(
            mappedBy = "gioHang",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<GioHangItem> items = new ArrayList<>();
}
