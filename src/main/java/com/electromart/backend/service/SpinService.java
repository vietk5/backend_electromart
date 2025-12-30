package com.electromart.backend.service;

import com.electromart.backend.dto.SpinResultResponse;
import com.electromart.backend.dto.SpinStatusResponse;
import com.electromart.backend.model.SpinHistory;
import com.electromart.backend.model.SpinReward;
import com.electromart.backend.model.SpinRewardType;
import com.electromart.backend.model.Voucher;
import com.electromart.backend.repository.SpinHistoryRepository;
import com.electromart.backend.repository.SpinRewardRepository;
import com.electromart.backend.repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SpinService {

    private final SpinRewardRepository rewardRepo;
    private final SpinHistoryRepository historyRepo;
    private final VoucherRepository voucherRepo;

    private static final int DAILY_LIMIT = 1;

    public SpinStatusResponse status(Long userId) {
        LocalDate today = LocalDate.now();
        long used = historyRepo.countByUserIdAndSpinDate(userId, today);
        boolean can = used < DAILY_LIMIT;

        return SpinStatusResponse.builder()
                .canSpin(can)
                .remainingSpinsToday((int) Math.max(0, DAILY_LIMIT - used))
                .message(can ? "Bạn còn 1 lượt quay hôm nay!" : "Hôm nay bạn đã quay rồi, quay lại ngày mai nhé.")
                .nextSpinAt(can ? null : tomorrow00hIso())
                .build();
    }

    private String tomorrow00hIso() {
        LocalDateTime tmr = LocalDate.now().plusDays(1).atStartOfDay();
        return tmr.toString();
    }

    @Transactional
    public SpinResultResponse spin(Long userId) {
        LocalDate today = LocalDate.now();
        if (historyRepo.countByUserIdAndSpinDate(userId, today) >= DAILY_LIMIT) {
            return SpinResultResponse.builder()
                    .success(false)
                    .label("Đã quay")
                    .type("MESSAGE")
                    .value("Bạn đã quay hôm nay rồi.")
                    .message("Hôm nay bạn đã quay rồi, quay lại ngày mai nhé.")
                    .build();
        }

        List<SpinReward> pool = new ArrayList<>(rewardRepo.findByActiveTrue());
        if (pool.isEmpty()) {
            return SpinResultResponse.builder()
                    .success(false)
                    .type("MESSAGE")
                    .label("Lỗi")
                    .value("Không có phần thưởng.")
                    .message("Hiện tại chưa có phần thưởng, thử lại sau nhé.")
                    .build();
        }

        SpinReward chosen = null;

        // reroll tối đa 10 lần nếu trúng voucher nhưng voucher không hợp lệ/hết lượt
        for (int attempt = 0; attempt < 10 && !pool.isEmpty(); attempt++) {
            SpinReward candidate = weightedPick(pool);
            if (candidate.getType() == SpinRewardType.VOUCHER) {
                String code = candidate.getValue();
                if (isVoucherAvailable(code)) {
                    chosen = candidate;
                    break;
                } else {
                    pool.remove(candidate); // loại ra để reroll
                }
            } else {
                chosen = candidate;
                break;
            }
        }

        if (chosen == null) {
            chosen = SpinReward.builder()
                    .label("Chúc bạn may mắn lần sau")
                    .type(SpinRewardType.MESSAGE)
                    .value("Chúc bạn may mắn lần sau 🍀")
                    .weight(1)
                    .active(true)
                    .build();
        }

        // Lưu lịch sử (chặn 1 lần/ngày bằng unique constraint)
        try {
            historyRepo.save(SpinHistory.builder()
                    .userId(userId)
                    .spinDate(today)
                    .label(chosen.getLabel())
                    .type(chosen.getType())
                    .value(chosen.getValue())
                    .createdAt(LocalDateTime.now())
                    .build());
        } catch (DataIntegrityViolationException ex) {
            // trường hợp user spam request 2 cái gần như cùng lúc
            return SpinResultResponse.builder()
                    .success(false)
                    .type("MESSAGE")
                    .label("Đã quay")
                    .value("Bạn đã quay hôm nay rồi.")
                    .message("Bạn đã quay hôm nay rồi, quay lại ngày mai nhé.")
                    .build();
        }

        // Trả về kết quả
        String msg;
        if (chosen.getType() == SpinRewardType.VOUCHER) {
            msg = "Bạn nhận được voucher: " + chosen.getValue();
        } else {
            msg = chosen.getValue();
        }

        return SpinResultResponse.builder()
                .success(true)
                .label(chosen.getLabel())
                .type(chosen.getType().name())
                .value(chosen.getValue())
                .message(msg)
                .build();
    }

    private SpinReward weightedPick(List<SpinReward> items) {
        int total = 0;
        for (SpinReward r : items) total += Math.max(0, r.getWeight());
        if (total <= 0) return items.get(new Random().nextInt(items.size()));

        int roll = new Random().nextInt(total);
        int cur = 0;
        for (SpinReward r : items) {
            cur += Math.max(0, r.getWeight());
            if (roll < cur) return r;
        }
        return items.get(items.size() - 1);
    }

    private boolean isVoucherAvailable(String code) {
        if (code == null || code.isBlank()) return false;

        Voucher v = voucherRepo.findByCodeIgnoreCase(code.trim()).orElse(null);
        if (v == null) return false;
        if (!v.isHoatDong()) return false;

        Instant now = Instant.now();
        if (v.getHieuLucTu() != null && now.isBefore(v.getHieuLucTu())) return false;
        if (v.getHieuLucDen() != null && now.isAfter(v.getHieuLucDen())) return false;

        int used = v.getSoLuongDaDung() == null ? 0 : v.getSoLuongDaDung();
        if (v.getSoLuongPhatHanh() != null && used >= v.getSoLuongPhatHanh()) return false;

        return true;
    }
}
