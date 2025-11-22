// KhachHang.java
package com.electromart.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter @Setter @AllArgsConstructor
@NoArgsConstructor
@Entity @Table(name = "khach_hang")

public class KhachHang extends NguoiDung {
    // có thể thêm field riêng cho KH nếu cần
    @Column(name = "bi_khoa", nullable = false)
    private boolean biKhoa = false; // mặc định false
}
