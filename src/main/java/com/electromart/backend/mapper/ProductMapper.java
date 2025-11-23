package com.electromart.backend.mapper;

import com.electromart.backend.dto.ProductDto;
import com.electromart.backend.dto.admin.AdminProductDto;
import com.electromart.backend.model.SanPham;

public class ProductMapper {

    // DTO cho client
    public static ProductDto toDto(SanPham e) {
        return new ProductDto(
                e.getId(),
                e.getTen(),
                e.getThuongHieu() != null ? e.getThuongHieu().getTen() : null,
                e.getLoai() != null ? e.getLoai().getTen() : null,
                e.getGia(),
                e.getImageUrl(),
                e.getMoTaNgan()
        );
    }

    // DTO cho trang ADMIN
    public static AdminProductDto toAdminDto(SanPham e) {
        Integer tonKho = e.getTonKho() == null ? 0 : e.getTonKho();

        return new AdminProductDto(
                e.getId(),
                e.getTen(),
                e.getGia(),
                tonKho,
                e.getLoai() != null ? e.getLoai().getTen() : null,
                e.getThuongHieu() != null ? e.getThuongHieu().getTen() : null,
                e.getImageUrl(),
                e.getMoTaNgan()
        );
    }
}
