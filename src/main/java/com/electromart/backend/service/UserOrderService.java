package com.electromart.backend.service;

import com.electromart.backend.dto.OrderDetailDto;
import com.electromart.backend.dto.OrderDetailItemDto;
import com.electromart.backend.mapper.OrderDetailMapper;
import com.electromart.backend.model.ChiTietDonHang;
import com.electromart.backend.model.DonHang;
import com.electromart.backend.model.TrangThaiDonHang;
import com.electromart.backend.repository.ChiTietDonHangRepository;
import com.electromart.backend.repository.DonHangRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserOrderService {

    private final DonHangRepository donHangRepository;
    private final OrderDetailMapper orderDetailMapper;
    private final ChiTietDonHangRepository chiTietDonHangRepository;

    @Transactional(readOnly = true)
    public OrderDetailDto getOrderDetail(Long orderId) {
        DonHang d = donHangRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy đơn hàng"));
        return orderDetailMapper.toDto(d);
    }

    // ✅ Lấy tất cả đơn của 1 khách hàng
    @Transactional(readOnly = true)
    public List<OrderDetailDto> getOrdersByUser(Long userId) {
        List<DonHang> list =
                donHangRepository.findByKhachHangIdOrderByNgayDatHangDesc(userId);

        return list.stream()
                .map(orderDetailMapper::toDto)
                .toList();
    }
    
    public void cancelOrder(Long orderId) {
        DonHang order = donHangRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        // ❗ Chỉ cho huỷ khi đang xử lý
        if (order.getTrangThai() != TrangThaiDonHang.DANG_XU_LY) {
            throw new RuntimeException("Không thể huỷ đơn ở trạng thái hiện tại");
        }

        order.setTrangThai(TrangThaiDonHang.DA_HUY);
        donHangRepository.save(order);
    }
    
    @Transactional(readOnly = true)
    public List<OrderDetailItemDto> getOrderItems(Long orderId) {
        // có thể kiểm tra đơn có tồn tại không, nếu muốn:
        if (!donHangRepository.existsById(orderId)) {
            throw new EntityNotFoundException("Không tìm thấy đơn hàng id=" + orderId);
        }

        List<ChiTietDonHang> list = chiTietDonHangRepository.findByDonHangId(orderId);
        return list.stream()
                .map(this::toItemDto)
                .toList();
    }
    private OrderDetailItemDto toItemDto(ChiTietDonHang ct) {
        OrderDetailItemDto dto = new OrderDetailItemDto();
        dto.setProductId(ct.getSanPham().getId());
        dto.setProductName(ct.getSanPham().getTen());
        dto.setSoLuong(ct.getSoLuong());
        dto.setDonGia(ct.getDonGia());        // BigDecimal
        dto.setThanhTien(ct.getThanhTien());  // BigDecimal
        dto.setImageUrl(ct.getSanPham().getImageUrl());
        return dto;
    }
}
