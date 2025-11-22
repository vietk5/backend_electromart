package com.electromart.backend.model;

public enum TrangThaiDonHang {
    MOI,            // mới tạo
    DANG_XU_LY,     // đã xác nhận, đang chuẩn bị
    DANG_GIAO,      // đang giao cho khách
    HOAN_THANH,     // giao thành công
    DA_HUY          // đã hủy
}
