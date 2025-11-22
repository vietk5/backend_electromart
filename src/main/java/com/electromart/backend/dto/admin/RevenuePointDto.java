// src/main/java/com/electromart/backend/dto/admin/RevenuePointDto.java
package com.electromart.backend.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevenuePointDto {
    /**
     * Nhãn để hiển thị (ví dụ: "2025-11-21", "2025-W47", "2025-11", "2025")
     */
    private String label;

    /**
     * Tổng doanh thu (VND) trong khoảng tương ứng
     */
    private BigDecimal total;
}
