package com.electromart.backend.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private Long id;
    private Long userId;
    private String tenNguoiNhan;
    private String soDienThoai;
    private String tinhThanhPho;
    private String quanHuyen;
    private String phuongXa;
    private String diaChiChiTiet;
    private String loaiDiaChi;
    private Boolean isDefault;
    private String createdAt;
    private String updatedAt;
    
    // Helper method để lấy địa chỉ đầy đủ
    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (diaChiChiTiet != null && !diaChiChiTiet.isEmpty()) {
            sb.append(diaChiChiTiet).append(", ");
        }
        if (phuongXa != null && !phuongXa.isEmpty()) {
            sb.append(phuongXa).append(", ");
        }
        if (quanHuyen != null && !quanHuyen.isEmpty()) {
            sb.append(quanHuyen).append(", ");
        }
        if (tinhThanhPho != null && !tinhThanhPho.isEmpty()) {
            sb.append(tinhThanhPho);
        }
        return sb.toString();
    }
}
