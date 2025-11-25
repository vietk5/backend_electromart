/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.electromart.backend.service;

import com.electromart.backend.dto.OrderDetailDto;
import com.electromart.backend.dto.OrderDetailItemDto;
import com.electromart.backend.model.ChiTietDonHang;
import com.electromart.backend.model.DonHang;
import com.electromart.backend.model.KhachHang;
import com.electromart.backend.model.SanPham;
import com.electromart.backend.repository.DonHangRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserOrderService {
    
    private final DonHangRepository donHangRepository;
    @Transactional
    public OrderDetailDto getOrderDetail(Long orderId) {
        DonHang d = donHangRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy đơn hàng"));

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

        dto.setCustomerName(kh.getHoTen());
        dto.setCustomerEmail(kh.getEmail());
        dto.setCustomerPhone(kh.getSoDienThoai());

        dto.setTongTien(total);
        dto.setPhuongThucThanhToan(d.getPhuongThuc().name());

        dto.setItems(items);

        return dto;
    }

}
