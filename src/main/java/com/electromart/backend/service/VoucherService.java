package com.electromart.backend.service;

import com.electromart.backend.dto.admin.*;
import com.electromart.backend.model.Voucher;
import com.electromart.backend.repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherService {

    private final VoucherRepository repo;

    public List<VoucherDto> listAll() {
        return repo.findAll().stream().map(this::toDto).toList();
    }

    @Transactional
    public VoucherDto create(CreateVoucherRequest req) {
        String code = normalizeCode(req.code);

        if (repo.existsByCodeIgnoreCase(code)) {
            throw new RuntimeException("Voucher đã tồn tại");
        }

        validateBusiness(req);

        Voucher v = new Voucher();
        v.setCode(code);
        v.setPhanTram(req.phanTram);
        v.setGiamToiDa(req.giamToiDa);
        v.setHieuLucTu(req.hieuLucTu);
        v.setHieuLucDen(req.hieuLucDen);
        v.setSoLuongPhatHanh(req.soLuongPhatHanh);
        v.setSoLuongDaDung(0);
        v.setHoatDong(req.hoatDong != null ? req.hoatDong : true);

        return toDto(repo.save(v));
    }

    @Transactional
    public VoucherDto update(Long id, CreateVoucherRequest req) {
        Voucher v = repo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy voucher"));

        validateBusiness(req);

        // code: nếu muốn cho đổi code thì check trùng
        if (req.code != null && !req.code.isBlank()) {
            String newCode = normalizeCode(req.code);
            if (!newCode.equalsIgnoreCase(v.getCode()) && repo.existsByCodeIgnoreCase(newCode)) {
                throw new RuntimeException("Mã voucher đã tồn tại");
            }
            v.setCode(newCode);
        }

        v.setPhanTram(req.phanTram);
        v.setGiamToiDa(req.giamToiDa);
        v.setHieuLucTu(req.hieuLucTu);
        v.setHieuLucDen(req.hieuLucDen);
        v.setSoLuongPhatHanh(req.soLuongPhatHanh);

        if (req.hoatDong != null) v.setHoatDong(req.hoatDong);

        return toDto(repo.save(v));
    }

    @Transactional
    public VoucherDto toggle(Long id, boolean hoatDong) {
        Voucher v = repo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy voucher"));
        v.setHoatDong(hoatDong);
        return toDto(repo.save(v));
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }

    public ApplyVoucherResponse apply(ApplyVoucherRequest req) {
        if (req == null || req.code == null || req.code.isBlank()) {
            return new ApplyVoucherResponse(false, "Bạn chưa nhập mã voucher",
                    BigDecimal.ZERO, req != null ? safe(req.tongTien) : BigDecimal.ZERO, null);
        }

        BigDecimal tongTien = safe(req.tongTien);

        Voucher v = repo.findByCodeIgnoreCase(req.code.trim())
                .orElse(null);

        if (v == null) {
            return new ApplyVoucherResponse(false, "Voucher không tồn tại", BigDecimal.ZERO, tongTien, null);
        }
        if (!v.isHoatDong()) {
            return new ApplyVoucherResponse(false, "Voucher đang tắt", BigDecimal.ZERO, tongTien, toDto(v));
        }

        Instant now = Instant.now();
        if (v.getHieuLucTu() != null && now.isBefore(v.getHieuLucTu())) {
            return new ApplyVoucherResponse(false, "Voucher chưa đến ngày hiệu lực", BigDecimal.ZERO, tongTien, toDto(v));
        }
        if (v.getHieuLucDen() != null && now.isAfter(v.getHieuLucDen())) {
            return new ApplyVoucherResponse(false, "Voucher đã hết hạn", BigDecimal.ZERO, tongTien, toDto(v));
        }

        if (v.getSoLuongPhatHanh() != null) {
            int used = v.getSoLuongDaDung() == null ? 0 : v.getSoLuongDaDung();
            if (used >= v.getSoLuongPhatHanh()) {
                return new ApplyVoucherResponse(false, "Voucher đã hết lượt sử dụng", BigDecimal.ZERO, tongTien, toDto(v));
            }
        }

        BigDecimal giam = BigDecimal.ZERO;

        if (v.getPhanTram() != null) {
            giam = tongTien
                    .multiply(BigDecimal.valueOf(v.getPhanTram()))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }

        if (v.getGiamToiDa() != null && giam.compareTo(v.getGiamToiDa()) > 0) {
            giam = v.getGiamToiDa();
        }

        if (giam.compareTo(BigDecimal.ZERO) < 0) giam = BigDecimal.ZERO;
        if (giam.compareTo(tongTien) > 0) giam = tongTien;

        BigDecimal tongSauGiam = tongTien.subtract(giam);

        return new ApplyVoucherResponse(true, "Áp dụng voucher thành công", giam, tongSauGiam, toDto(v));
    }

    // ===== helpers =====

    private VoucherDto toDto(Voucher v) {
        return new VoucherDto(
                v.getId(),
                v.getCode(),
                v.getPhanTram(),
                v.getGiamToiDa(),
                v.getHieuLucTu(),
                v.getHieuLucDen(),
                v.getSoLuongPhatHanh(),
                v.getSoLuongDaDung(),
                v.isHoatDong()
        );
    }

    private void validateBusiness(CreateVoucherRequest req) {
        // Cho phép: percent null, max null (hoặc cả 2 đều có)
        if (req.phanTram == null && (req.giamToiDa == null || req.giamToiDa.compareTo(BigDecimal.ZERO) <= 0)) {
            // Nếu bạn muốn bắt buộc phải có ít nhất 1 kiểu giảm thì bật cái này:
            // throw new RuntimeException("Cần nhập % giảm hoặc giảm tối đa");
        }

        if (req.hieuLucTu != null && req.hieuLucDen != null && req.hieuLucDen.isBefore(req.hieuLucTu)) {
            throw new RuntimeException("Ngày hết hạn phải sau ngày bắt đầu");
        }
    }

    private String normalizeCode(String code) {
        return code == null ? null : code.trim();
    }

    private BigDecimal safe(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v.max(BigDecimal.ZERO);
    }
    @Transactional
    public void consumeOnCheckout(String code) {
        if (code == null || code.isBlank()) return;

        Voucher v = repo.findByCodeIgnoreCase(code.trim()).orElse(null);
        if (v == null) throw new RuntimeException("Voucher không tồn tại");
        if (!v.isHoatDong()) throw new RuntimeException("Voucher đang tắt");

        Instant now = Instant.now();
        if (v.getHieuLucTu() != null && now.isBefore(v.getHieuLucTu()))
            throw new RuntimeException("Voucher chưa đến ngày hiệu lực");
        if (v.getHieuLucDen() != null && now.isAfter(v.getHieuLucDen()))
            throw new RuntimeException("Voucher đã hết hạn");

        int used = v.getSoLuongDaDung() == null ? 0 : v.getSoLuongDaDung();
        if (v.getSoLuongPhatHanh() != null && used >= v.getSoLuongPhatHanh())
            throw new RuntimeException("Voucher đã hết lượt sử dụng");

        v.setSoLuongDaDung(used + 1);
        repo.save(v);
    }
}
