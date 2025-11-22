// service/AdminDashboardService.java (bản dùng DonHang)
package com.electromart.backend.service;

import com.electromart.backend.dto.admin.AdminDashboardSummaryDto;
import com.electromart.backend.dto.admin.LowStockProductDto;
import com.electromart.backend.dto.admin.RecentOrderDto;
import com.electromart.backend.model.SanPham;
import com.electromart.backend.model.DonHang;
import com.electromart.backend.model.ChiTietDonHang;
import com.electromart.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final KhachHangRepository khachHangRepo;
    private final SanPhamRepository sanPhamRepo;
    private final DonHangRepository donHangRepo;

    public AdminDashboardSummaryDto getSummary() {
        AdminDashboardSummaryDto dto = new AdminDashboardSummaryDto();
        dto.setTotalCustomers(khachHangRepo.count());
        dto.setTotalProducts(sanPhamRepo.count());
        dto.setTotalStock(sanPhamRepo.sumAllStock());      // đã thêm ton_kho ở SanPham
        dto.setTotalOrders(donHangRepo.count());
        return dto;
    }

    public List<LowStockProductDto> getLowStockTop10() {
        return sanPhamRepo.findLowStock(PageRequest.of(0, 10))
                .getContent()
                .stream()
                .map(this::toLowStockDto)
                .collect(Collectors.toList());
    }

    private LowStockProductDto toLowStockDto(SanPham s) {
        LowStockProductDto dto = new LowStockProductDto();
        dto.setId(s.getId());
        dto.setName(s.getTen());
        dto.setImageUrl(s.getImageUrl());
        dto.setStock(s.getTonKho());
        return dto;
    }

    public List<RecentOrderDto> getRecentOrdersTop10() {
        return donHangRepo.findAllByOrderByNgayDatHangDesc(PageRequest.of(0, 10))
                .getContent()
                .stream()
                .map(this::toRecentOrderDto)
                .collect(Collectors.toList());
    }

    private RecentOrderDto toRecentOrderDto(DonHang d) {
        RecentOrderDto dto = new RecentOrderDto();
        dto.setId(d.getId());
        dto.setCode("DH-" + d.getId());             // nếu chưa có mã đơn riêng
        dto.setCreatedAt(d.getNgayDatHang());
        dto.setStatus(d.getTrangThai().name());
        dto.setCustomerName(d.getKhachHang().getHoTen());
        dto.setTotalAmount(tinhTongTien(d).longValue());
        return dto;
    }

    private BigDecimal tinhTongTien(DonHang d) {
        return d.getChiTiet().stream()
                .map(ChiTietDonHang::getThanhTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
