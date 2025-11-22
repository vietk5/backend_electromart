package com.electromart.backend.service;

import com.electromart.backend.dto.admin.AdminOrderDto;
import com.electromart.backend.model.ChiTietDonHang;
import com.electromart.backend.model.DonHang;
import com.electromart.backend.model.KhachHang;
import com.electromart.backend.model.TrangThaiDonHang;
import com.electromart.backend.repository.DonHangRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminOrderService {

    private final DonHangRepository donHangRepository;

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

    private AdminOrderDto toDto(DonHang d) {
        // Tính tổng tiền từ list chi tiết
        BigDecimal total = d.getChiTiet().stream()
                .map(ChiTietDonHang::getThanhTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        KhachHang kh = d.getKhachHang();

        return AdminOrderDto.builder()
                .id(d.getId())
                .customerName(kh != null ? kh.getHoTen() : null) // nếu field khác thì sửa lại
                .totalAmount(total)
                .status(d.getTrangThai())
                .ngayDatHang(d.getNgayDatHang())
                .build();
    }
}
