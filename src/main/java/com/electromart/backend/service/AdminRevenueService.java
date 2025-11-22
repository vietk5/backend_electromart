// AdminRevenueService.java
package com.electromart.backend.service;

import com.electromart.backend.dto.admin.RevenuePointDto;
import com.electromart.backend.model.DonHang;
import com.electromart.backend.model.TrangThaiDonHang;
import com.electromart.backend.repository.DonHangRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminRevenueService {

    private final DonHangRepository donHangRepository;

    public enum GroupBy { DAY, MONTH, YEAR }

    public AdminRevenueService(DonHangRepository donHangRepository) {
        this.donHangRepository = donHangRepository;
    }

    @Transactional(readOnly = true) // ✅ quan trọng
    public List<RevenuePointDto> getRevenue(LocalDate from, LocalDate to, GroupBy groupBy) {

        LocalDateTime fromDt = from.atStartOfDay();
        LocalDateTime toDt   = to.plusDays(1).atStartOfDay(); // để bao hết ngày "to"

        // ✅ LẤY ĐƠN HÀNG + CHI TIẾT ĐÃ FETCH
        List<DonHang> orders = donHangRepository.findRevenueOrders(
                TrangThaiDonHang.HOAN_THANH, // chỉ tính đơn đã hoàn thành; nếu muốn mọi trạng thái thì bỏ filter này
                fromDt,
                toDt
        );

        Map<Object, BigDecimal> map = new TreeMap<>();

        for (DonHang dh : orders) {
            // tính tổng tiền 1 đơn
            BigDecimal total = dh.getChiTiet().stream()
                    .map(ct -> ct.getDonGia().multiply(BigDecimal.valueOf(ct.getSoLuong())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Object key;
            switch (groupBy) {
                case DAY -> key = dh.getNgayDatHang().toLocalDate();
                case MONTH -> {
                    LocalDate d = dh.getNgayDatHang().toLocalDate();
                    key = YearMonth.of(d.getYear(), d.getMonth());
                }
                case YEAR -> key = dh.getNgayDatHang().getYear();
                default -> throw new IllegalArgumentException("Invalid groupBy");
            }

            map.merge(key, total, BigDecimal::add);
        }

        // convert về list dto – tuỳ em định nghĩa RevenuePointDto
        return map.entrySet().stream()
                .map(e -> {
                    Object k = e.getKey();
                    BigDecimal v = e.getValue();
                    // ví dụ: RevenuePointDto(String label, BigDecimal amount)
                    String label;
                    if (k instanceof LocalDate d) label = d.toString();
                    else if (k instanceof YearMonth ym) label = ym.toString();
                    else label = String.valueOf(k); // year

                    return new RevenuePointDto(label, v);
                })
                .collect(Collectors.toList());
    }
}
