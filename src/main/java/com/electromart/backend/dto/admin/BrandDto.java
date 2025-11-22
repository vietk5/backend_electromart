package com.electromart.backend.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Nếu dùng Lombok. Nếu không thì tự viết Getter/Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrandDto {
    private Long id;
    private String ten;
}