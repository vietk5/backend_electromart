package com.electromart.backend.service;

import com.electromart.backend.dto.OrderDetailDto;
import com.electromart.backend.mapper.OrderDetailMapper;
import com.electromart.backend.model.DonHang;
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
}
