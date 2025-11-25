package com.electromart.backend.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class OrderDetailItemDto {
    private Long productId;
    private String productName;
    private String imageUrl;
    private Integer soLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
}