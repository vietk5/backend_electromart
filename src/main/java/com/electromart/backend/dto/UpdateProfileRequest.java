package com.electromart.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateProfileRequest {
    private String hoTen;
    private String soDienThoai;
    private String diaChi;
}
