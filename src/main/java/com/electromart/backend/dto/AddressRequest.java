package com.electromart.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {
    
    @NotNull(message = "User ID không được để trống")
    private Long userId;
    
    @NotBlank(message = "Tên người nhận không được để trống")
    private String tenNguoiNhan;
    
    @NotBlank(message = "Số điện thoại không được để trống")
    private String soDienThoai;
    
    @NotBlank(message = "Tỉnh/Thành phố không được để trống")
    private String tinhThanhPho;
    
    @NotBlank(message = "Quận/Huyện không được để trống")
    private String quanHuyen;
    
    @NotBlank(message = "Phường/Xã không được để trống")
    private String phuongXa;
    
    @NotBlank(message = "Địa chỉ chi tiết không được để trống")
    private String diaChiChiTiet;
    
    private String loaiDiaChi; // "Nhà" hoặc "Văn phòng"
    
    private Boolean isDefault;
}
