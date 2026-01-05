package com.electromart.backend.dto;

import com.electromart.backend.model.TrangThaiDonHang;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class OrderDetailDto {

    private Long id;
    private LocalDateTime ngayDatHang;
    private TrangThaiDonHang trangThai;

    private String tenNguoiNhan;
    private String soDienThoaiNhan;
    private String diaChiNhan;

    private BigDecimal tongTien;
    private String phuongThucThanhToan;

    private List<OrderDetailItemDto> items;
}
