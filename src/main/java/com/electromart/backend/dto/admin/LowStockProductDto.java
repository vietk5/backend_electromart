package com.electromart.backend.dto.admin;


import lombok.Data;

@Data
public class LowStockProductDto {
    private Long id;
    private String name;
    private String imageUrl;
    private int stock; // tồn hiện tại
}
