package com.electromart.backend.dto.admin;

import com.electromart.backend.model.TrangThaiDonHang;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminOrderDto {

    private Long id;
    private String customerName;      // Tên khách
    private BigDecimal totalAmount;   // Tổng tiền (tính từ chi tiết)
    private TrangThaiDonHang status;  // Trạng thái đơn
    private LocalDateTime ngayDatHang; // Ngày đặt hàng
}
