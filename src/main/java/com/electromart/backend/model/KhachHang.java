package com.electromart.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "khach_hang")
public class KhachHang extends NguoiDung {

    // cột để block / unblock tài khoản
    @Column(name = "bi_khoa", nullable = false)
    private Boolean biKhoa = false;

    public Boolean getBiKhoa() {
        return biKhoa != null ? biKhoa : false;
    }
}
