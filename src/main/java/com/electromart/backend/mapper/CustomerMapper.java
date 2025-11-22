package com.electromart.backend.mapper;


import com.electromart.backend.dto.admin.AdminCustomerDto;
import com.electromart.backend.model.KhachHang;

public class CustomerMapper {

    public static AdminCustomerDto toAdminDto(KhachHang e) {
        return new AdminCustomerDto(
                e.getId(),                    // từ AuditEntity
                e.getHoTen(),                 // từ NguoiDung
                e.getEmail(),                 // từ NguoiDung
                e.getSoDienThoai(),           // từ NguoiDung
                e.getCreatedAt(),             // từ AuditEntity
                e.getBiKhoa()                 // từ KhachHang
        );
    }
}
