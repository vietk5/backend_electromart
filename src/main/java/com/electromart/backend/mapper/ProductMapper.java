package com.electromart.backend.mapper;

import com.electromart.backend.dto.ProductDto;
import com.electromart.backend.model.SanPham;

public class ProductMapper {
    public static ProductDto toDto(SanPham e) {
        return new ProductDto(
                e.getId(),
                e.getTen(),
                e.getThuongHieu()!=null? e.getThuongHieu().getTen(): null,
                e.getLoai()!=null? e.getLoai().getTen(): null,
                e.getGia(),
                e.getImageUrl()
        );
    }
}
