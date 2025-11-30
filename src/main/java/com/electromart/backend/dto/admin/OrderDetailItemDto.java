package com.electromart.backend.dto.admin;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailItemDto {
    private String productName;
    private Integer soLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private String imageUrl;
}
