package com.electromart.backend.mapper;

import com.electromart.backend.dto.OrderDetailDto;
import com.electromart.backend.dto.OrderDetailItemDto;
import com.electromart.backend.model.ChiTietDonHang;
import com.electromart.backend.model.DonHang;
import com.electromart.backend.model.KhachHang;
import com.electromart.backend.model.SanPham;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class OrderDetailMapper {

    public OrderDetailDto toDto(DonHang d) {
        // Tính tổng tiền
        BigDecimal total = d.getChiTiet().stream()
                .map(ChiTietDonHang::getThanhTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Chuyển chi tiết sản phẩm sang DTO
        List<OrderDetailItemDto> items = d.getChiTiet().stream().map(ct -> {
            SanPham sp = ct.getSanPham();

            OrderDetailItemDto item = new OrderDetailItemDto();
            item.setProductId(sp.getId());
            item.setProductName(sp.getTen());
            item.setImageUrl(sp.getImageUrl());
            item.setDonGia(ct.getDonGia());
            item.setSoLuong(ct.getSoLuong());
            item.setThanhTien(ct.getThanhTien());
            return item;
        }).toList();

        KhachHang kh = d.getKhachHang();

        OrderDetailDto dto = new OrderDetailDto();
        dto.setId(d.getId());
        dto.setNgayDatHang(d.getNgayDatHang());
        dto.setTrangThai(d.getTrangThai());

        dto.setTenNguoiNhan(d.getTenNguoiNhan());
        dto.setSoDienThoaiNhan(d.getSoDienThoaiNhan());
        dto.setDiaChiNhan(d.getDiaChiNhan());


        dto.setTongTien(total);
        dto.setPhuongThucThanhToan(d.getPhuongThuc().name());
        dto.setItems(items);

        return dto;
    }
}
