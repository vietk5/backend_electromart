package com.electromart.backend.service;

import com.electromart.backend.dto.admin.AdminOrderDto;
import com.electromart.backend.dto.admin.OrderDetailItemDto; // <-- import DTO item
import com.electromart.backend.model.ChiTietDonHang;
import com.electromart.backend.model.DonHang;
import com.electromart.backend.model.KhachHang;
import com.electromart.backend.model.TrangThaiDonHang;
import com.electromart.backend.repository.ChiTietDonHangRepository; // <-- thêm
import com.electromart.backend.repository.DonHangRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminOrderService {

    private final DonHangRepository donHangRepository;
    private final ChiTietDonHangRepository chiTietDonHangRepository; // <-- thêm

    public Page<AdminOrderDto> getOrders(Pageable pageable) {
        return donHangRepository.findAll(pageable)
                .map(this::toDto);
    }

    public void updateStatus(Long orderId, TrangThaiDonHang status) {
        DonHang donHang = donHangRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy đơn hàng id=" + orderId));

        donHang.setTrangThai(status);
        donHangRepository.save(donHang);
    }

    // ====== HÀM MỚI: LẤY DANH SÁCH SẢN PHẨM TRONG ĐƠN ======
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
        dto.setProductName(ct.getSanPham().getTen());
        dto.setSoLuong(ct.getSoLuong());
        dto.setDonGia(ct.getDonGia());        // BigDecimal
        dto.setThanhTien(ct.getThanhTien());  // BigDecimal
        dto.setImageUrl(ct.getSanPham().getImageUrl());
        return dto;
    }

    // ====== DTO ĐƠN TẮT CHO LIST ======
    private AdminOrderDto toDto(DonHang d) {
        // Tính tổng tiền từ list chi tiết
        BigDecimal total = d.getChiTiet().stream()
                .map(ChiTietDonHang::getThanhTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        KhachHang kh = d.getKhachHang();

        return AdminOrderDto.builder()
                .id(d.getId())
                .customerName(kh != null ? kh.getHoTen() : null)
                .totalAmount(total)
                .status(d.getTrangThai())
                .ngayDatHang(d.getNgayDatHang())
                .build();
    }
}
